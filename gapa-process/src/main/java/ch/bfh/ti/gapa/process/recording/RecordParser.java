package ch.bfh.ti.gapa.process.recording;

import ch.bfh.ti.gapa.domain.recording.Record;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ch.bfh.ti.gapa.process.recording.InboundRequestPatternGroup.*;

/**
 * Created by adrian on 20.10.17.
 */
public class RecordParser {
    private Pattern logPattern;
    private Pattern urlPattern;
    private DateTimeFormatter timeFormat;

    public RecordParser(Pattern logPattern, Pattern urlPattern, DateTimeFormatter timeFormat) {
        this.logPattern = logPattern;
        this.urlPattern = urlPattern;
        this.timeFormat = timeFormat;
    }

    public Record parse(String logEntry) {
        Matcher matcher = logPattern.matcher(logEntry);

        if(matcher.find()) {
            String dateString = matcher.group(DATE.getName());
            LocalDateTime time = LocalDateTime.parse(dateString, timeFormat);
            String httpMethod = matcher.group(METHOD.getName());
            String targetUrl = matcher.group(URL.getName());
            String sender = matcher.group(SENDER.getName());

            Record record = new Record();

            Matcher targetMatcher = urlPattern.matcher(targetUrl);
            if(targetMatcher.find()) {
                String target = targetMatcher.group("target");
                record.setRecipient(target);
            } else {
                record.setRecipient(null);
            }
            record.setUrl(targetUrl);
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
