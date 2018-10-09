package com.ewind.hl.export;

import android.support.annotation.NonNull;
import android.util.Log;

import com.ewind.hl.model.area.AreaFactory;
import com.ewind.hl.model.event.Event;
import com.ewind.hl.model.event.Score;
import com.ewind.hl.model.event.detail.EventDetail;
import com.ewind.hl.model.event.type.EventType;
import com.ewind.hl.model.event.type.EventTypeFactory;
import com.ewind.hl.persist.EventDateConverter;
import com.fasterxml.jackson.databind.ObjectMapper;

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

    private static final String[] CSV_HEADER = { "id", "date", "type", "area", "score", "detail", "note" };
    private static final CSVFormat WRITE_FORMAT = CSVFormat.DEFAULT.withHeader(CSV_HEADER);
    private static final CSVFormat READ_FORMAT = WRITE_FORMAT.withSkipHeaderRecord();
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public void export(Appendable out, List<Event> events) throws IOException {
        Log.i(TAG, "Export " + events.size() + " events");

        try (CSVPrinter printer = new CSVPrinter(out, WRITE_FORMAT)) {
            for (Event event : events) {
                printer.printRecord(toColumns(event));
            }
            printer.flush();
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
                    MAPPER.writeValueAsString(event.getDetail()),
                    event.getNote()
            };
        } catch (Exception e) {
            Log.e(TAG, "Failed to export event '" + event.getId() + "'", e);
            return toFailedColumns(event);
        }
    }

    @NonNull
    private <D extends EventDetail> Event<D> toEvent(CSVRecord record) throws IOException {
        EventType<D> type = EventTypeFactory.get(record.get(2));
        return new Event<>(Long.parseLong(record.get(0)),
                EventDateConverter.deserialize(record.get(1)),
                type,
                MAPPER.readValue(record.get(5), type.getDetailClass()),
                AreaFactory.getArea(record.get(3)),
                record.get(6),
                new Score(Integer.parseInt(record.get(4)))
        );
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

    public List<Event> read(InputStream in) throws IOException {
        List<Event> result = new LinkedList<>();
        try (CSVParser parser = new CSVParser(new InputStreamReader(in), READ_FORMAT)) {
            for (CSVRecord record : parser) {
                result.add(toEvent(record));
            }
        }
        return result;
    }
}
