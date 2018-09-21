package com.ewind.hl.export;

import android.os.AsyncTask;
import android.util.Log;

import com.ewind.hl.model.event.Event;
import com.ewind.hl.persist.EventsDao;

import java.util.List;

public class ExportTask extends AsyncTask<ExportTask.ExportListener, Void, String> {

    private static final String TAG = ExportTask.class.getSimpleName();

    private final FileExporter fileExporter;
    private final EventsDao dao;

    private ExportListener[] listeners;

    public interface ExportListener {
        void onExportFinished(String exportPath);
    }

    public ExportTask(EventsDao dao) {
        this.dao = dao;
        this.fileExporter = new FileExporter();
    }

    @Override
    protected String doInBackground(ExportListener... listeners) {
        this.listeners = listeners;
        Log.i(TAG, "Start exporting");
        List<Event> events = dao.getAll();
        Log.i(TAG, "" + events.size() + " events found for export");
        return fileExporter.export(events);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String exportPath) {
        Log.i(TAG, "Export finished: " + exportPath);
        for (ExportListener listener : listeners) {
            listener.onExportFinished(exportPath);
        }
    }
}
