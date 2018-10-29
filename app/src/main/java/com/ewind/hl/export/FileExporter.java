package com.ewind.hl.export;

import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import com.ewind.hl.persist.EventEntity;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class FileExporter {

    private static final String TAG = FileExporter.class.getSimpleName();

    private static final String EXPORT_FILE_NAME = "Health Log export.csv";

    private final CsvExporter exporter;

    public FileExporter() {
        this.exporter = new CsvExporter();
    }

    public String export(List<EventEntity> events) {
        Log.i(TAG, "Export " + events.size() + " events");

        try {
            File export = getFile();

            try (FileWriter out = new FileWriter(export)) {
                exporter.export(out, events);
            }

            Log.i(TAG, "Events exported to file: " + export.getAbsolutePath());
            return export.getAbsolutePath();
        } catch (Exception e) {
            throw new IllegalStateException("Failed to export to file", e);
        }
    }

    @NonNull
    private File getFile() throws IOException {
        File documentsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);

        File export = new File(documentsDir, EXPORT_FILE_NAME);
        if (!export.exists()) {
            boolean created = export.createNewFile();
            Log.i(TAG, "New export file '" + export.getAbsolutePath() + "' created: " + created);
        } else {
            Log.i(TAG, "Export file found: " + export.getAbsolutePath());
        }
        return export;
    }

}
