package com.ewind.hl.export;

import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import com.ewind.hl.model.event.Event;
import com.ewind.hl.persist.EventDateConverter;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

public class FileExporter {

    private static final String TAG = FileExporter.class.getSimpleName();

    private static final String EXPORT_FILE_NAME = "Health Log export.csv";
    private static final String[] CSV_HEADER = { "id", "date", "type", "area", "score", "detail", "note" };
    private static final CSVFormat FORMAT = CSVFormat.DEFAULT.withHeader(CSV_HEADER);

    public String export(List<Event> events) {
        Log.i(TAG, "Export " + events.size() + " events");

        try {
            File documentsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);

            File export = new File(documentsDir, EXPORT_FILE_NAME);
            if (!export.exists()) {
                boolean created = export.createNewFile();
                Log.i(TAG, "New export file '" + export.getAbsolutePath() + "' created: " + created);
            } else {
                Log.i(TAG, "Export file found: " + export.getAbsolutePath());
            }

            try (FileWriter out = new FileWriter(export);
                 CSVPrinter printer = new CSVPrinter(out, FORMAT)) {
                for (Event event : events) {
                    printer.printRecord(toColumns(event));
                }
                printer.flush();
            }

            Log.i(TAG, "Events exported to file: " + export.getAbsolutePath());
            return export.getAbsolutePath();
        } catch (Exception e) {
            throw new IllegalStateException("Failed to export to file", e);
        }
    }

    @NonNull
    private Object[] toColumns(Event event) {
        try {
            return new Object[]{
                    event.getId(),
                    EventDateConverter.serialize(event.getDate()),
                    event.getType().getName(),
                    event.getArea().getName(),
                    event.getScore().getValue(),
                    new ObjectMapper().writeValueAsString(event.getDetail()),
                    event.getNote()
            };
        } catch (Exception e) {
            Log.e(TAG, "Failed to export event '" + event.getId() + "'", e);
            return toFailedColumns(event);
        }
    }

    @NonNull
    private String[] toFailedColumns(Event event) {
        return new String[]{
                String.valueOf(event.getId()),
                null,
                null,
                null,
                null,
                null,
                null
        };
    }
}
