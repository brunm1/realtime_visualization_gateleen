package ch.bfh.ti.gapa.process.recording;

import ch.bfh.ti.gapa.domain.recording.Record;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by adrian on 20.10.17.
 */
public class RecordParser {
    private Pattern pattern;
    private DateTimeFormatter timeFormat;

    public RecordParser(Pattern pattern, DateTimeFormatter timeFormat) {
        this.pattern = pattern;
        this.timeFormat = timeFormat;
    }

    public Record parse(String logEntry) {
        Matcher matcher = pattern.matcher(logEntry);

        if(matcher.find()) {
            String dateString = matcher.group("date");
            LocalDateTime time = LocalDateTime.parse(dateString, timeFormat);
            String httpMethod = matcher.group("method");
            String targetUrl = matcher.group("url");
            String sender = matcher.group("sender");

            Record record = new Record();
            record.setReciepent(targetUrl); //ToDo: ev. shorten or resolve
            record.setSender(sender);
            record.setTime(time);
            record.setHttpMethod(httpMethod);

            return record;
        } else {
            return null;
        }
    }

    public List<Record> batchParse(String log) {
        List<Record> records = new ArrayList<>();

        Arrays.stream(log.split(System.lineSeparator())).map(this::parse).filter(r -> r != null).forEach(records::add);

        return records;
    }
}
