package com.ewind.hl.export;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.ewind.hl.persist.AppDatabase;
import com.ewind.hl.persist.EventEntity;
import com.ewind.hl.persist.EventEntityDao;

import java.util.List;

public class ImportTask extends AsyncTask<ImportTask.ImportListener, Void, String> {

    private static final String TAG = ImportTask.class.getSimpleName();

    private static final String IMPORT_NOT_ALLOWED = "Cannot import when local events exist";

    private final DriveAdapter adapter;
    private final EventEntityDao dao;
    private ImportListener[] listeners;

    public interface ImportListener {
        void onImportFinished(String importedNumber);
    }

    public ImportTask(Context context) {
        this.dao = AppDatabase.getInstance(context).eventEntityDao();
        this.adapter = new DriveAdapter(context);
    }

    @Override
    protected String doInBackground(ImportListener... listeners) {
        this.listeners = listeners;
        Log.i(TAG, "Start importing");
        List<EventEntity> events = adapter.read();
        int size = events.size();
        Log.i(TAG, "" + size + " events found for import");

        List<EventEntity> existingEvents = dao.getAll();
        if (!existingEvents.isEmpty()) {
            String warnMessage = IMPORT_NOT_ALLOWED + ": " + existingEvents.size();
            Log.w(TAG, warnMessage);
            return warnMessage;
        } else {
            for (EventEntity event : events) {
                dao.insert(event);
                Log.i(TAG, "Event imported: " + event);
            }
        }

        return String.valueOf(size);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String importedNumber) {
        Log.i(TAG, "Import finished: " + importedNumber);
        for (ImportListener listener : listeners) {
            listener.onImportFinished(importedNumber);
        }
    }
}
