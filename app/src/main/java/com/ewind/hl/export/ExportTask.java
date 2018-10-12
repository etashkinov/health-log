package com.ewind.hl.export;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.ewind.hl.model.event.Event;
import com.ewind.hl.persist.EventsDao;

import java.util.List;

public class ExportTask extends AsyncTask<ExportTask.ExportListener, Void, Integer> {

    private static final String TAG = ExportTask.class.getSimpleName();

    private final DriveAdapter adapter;
    private final EventsDao dao;
    private ExportListener[] listeners;

    public interface ExportListener {
        void onExportFinished(int exportPath);
    }

    public ExportTask(Context context) {
        this.dao = new EventsDao(context);
        this.adapter = new DriveAdapter(context);
    }

    @Override
    protected Integer doInBackground(ExportListener... listeners) {
        this.listeners = listeners;
        Log.i(TAG, "Start exporting");
        List<Event> events = dao.getAll();
        Log.i(TAG, "" + events.size() + " events found for export");
        adapter.export(events);
        return events.size();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Integer exportNumber) {
        Log.i(TAG, "Export finished: " + exportNumber);
        for (ExportListener listener : listeners) {
            listener.onExportFinished(exportNumber);
        }
    }
}
