package com.ewind.hl.export;

import android.support.annotation.NonNull;
import android.util.Log;

import com.ewind.hl.persist.EventEntity;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

public class CsvExporter {

    private static final String TAG = CsvExporter.class.getSimpleName();

    private static final String[] CSV_HEADER = { "id", "date", "type", "area", "score", "detail", "note", "owner", "reporter" };
    private static final CSVFormat WRITE_FORMAT = CSVFormat.DEFAULT.withHeader(CSV_HEADER);
    private static final CSVFormat READ_FORMAT = WRITE_FORMAT.withSkipHeaderRecord();

    public void export(Appendable out, List<EventEntity> events) throws IOException {
        Log.i(TAG, "Export " + events.size() + " events");

        try (CSVPrinter printer = new CSVPrinter(out, WRITE_FORMAT)) {
            for (EventEntity event : events) {
                printer.printRecord(toColumns(event));
            }
            printer.flush();
        }
    }

    @NonNull
    private Object[] toColumns(EventEntity event) {
        try {
            return new Object[]{
                    event.getId(),
                    event.getDate(),
                    event.getType(),
                    event.getArea(),
                    event.getScore(),
                    event.getValue(),
                    event.getNote(),
                    event.getOwner(),
                    event.getReporter()
            };
        } catch (Exception e) {
            Log.e(TAG, "Failed to export event '" + event.getId() + "'", e);
            return toFailedColumns(event);
        }
    }

    @NonNull
    private EventEntity toEvent(CSVRecord record) {
        EventEntity result = new EventEntity();
        result.setId(Long.parseLong(record.get(0)));
        result.setDate(record.get(1));
        result.setType(record.get(2));
        result.setValue(record.get(5));
        result.setArea(record.get(3));
        result.setNote(record.get(6));
        result.setScore(Integer.parseInt(record.get(4)));
        result.setOwner(record.get(7));
        result.setReporter(record.get(8));
        return result;
    }

    @NonNull
    private String[] toFailedColumns(EventEntity event) {
        return new String[]{
                String.valueOf(event.getId()),
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        };
    }

    public List<EventEntity> read(InputStream in) throws IOException {
        List<EventEntity> result = new LinkedList<>();
        try (CSVParser parser = new CSVParser(new InputStreamReader(in), READ_FORMAT)) {
            for (CSVRecord record : parser) {
                result.add(toEvent(record));
            }
        }
        return result;
    }
}
