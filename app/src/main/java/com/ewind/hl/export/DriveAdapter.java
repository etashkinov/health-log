package com.ewind.hl.export;

import android.content.Context;
import android.util.Log;

import com.ewind.hl.model.event.Event;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.DriveResourceClient;
import com.google.android.gms.drive.MetadataBuffer;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.drive.query.Filters;
import com.google.android.gms.drive.query.Query;
import com.google.android.gms.drive.query.SearchableField;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class DriveAdapter {

    private static final String TAG = DriveAdapter.class.getSimpleName();

    private static final String EXPORT_FILE_NAME = "Health Log export.csv";
    private final CsvExporter exporter;
    private DriveResourceClient driveResourceClient;

    public DriveAdapter(Context context) {
        this.exporter = new CsvExporter();

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(context);
        driveResourceClient = Drive.getDriveResourceClient(context, account);
    }

    public void export(List<Event> events) {
        Log.i(TAG, "Export " + events.size() + " events");

        try {
            createFileInAppFolder(events);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public List<Event> read() {
        try {
            DriveContents contents = getExportFileContents();

            try (InputStream in = contents.getInputStream()) {
                return exporter.read(in);
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private DriveContents getExportFileContents() throws Exception {
        Task<DriveContents> driveContentsTask = driveResourceClient.getAppFolder().continueWithTask(t -> {
            DriveFolder appFolder = t.getResult();
            Query query = new Query.Builder()
                    .addFilter(Filters.eq(SearchableField.TITLE, EXPORT_FILE_NAME))
                    .build();

            return driveResourceClient.queryChildren(appFolder, query);
        }).continueWithTask(t -> {
            MetadataBuffer result = t.getResult();
            DriveId driveId = result.get(0).getDriveId();
            return driveResourceClient.openFile(driveId.asDriveFile(), DriveFile.MODE_READ_ONLY);
        });

        Tasks.await(driveContentsTask);
        return driveContentsTask.getResult();
    }

    private void createFileInAppFolder(List<Event> events) throws ExecutionException, InterruptedException {
        Task<DriveFolder> appFolderTask = driveResourceClient.getAppFolder();
        Task<DriveContents> contentsTask = driveResourceClient.createContents();
        Task<DriveFile> resultTask = Tasks.whenAll(appFolderTask, contentsTask).continueWithTask(t -> {
                    MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                            .setTitle(EXPORT_FILE_NAME)
                            .setMimeType("text/csv")
                            .setStarred(true)
                            .build();

                    DriveContents contents = contentsTask.getResult();
                    OutputStream outputStream = contents.getOutputStream();
                    try (Writer writer = new OutputStreamWriter(outputStream)) {
                        exporter.export(writer, events);
                    }

                    return driveResourceClient.createFile(appFolderTask.getResult(), changeSet, contents);
                }
        );
        Tasks.await(resultTask);
    }

}
