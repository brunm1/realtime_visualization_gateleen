package ch.bfh.ti.gapa.process.recording;

import ch.bfh.ti.gapa.domain.recording.Record;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class RecordParserTest {
    static Pattern pattern;
    static DateTimeFormatter timeFormat;

    @BeforeAll
    static void initialize() {
        pattern = Pattern.compile("^(?<date>\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2},\\d{3})\\s+(\\S+)\\s+(\\S+)\\s+(\\S+)\\s+(\\S+) - %(\\S+)\\s+(?<method>GET|PUT|POST|DELETE)\\s+(?<url>\\S+)\\s+s=(?<sender>\\w+)");
        timeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss,SSS", Locale.GERMANY);

    }


    @Test
    void parse() {
        String data = "2017-10-20 09:11:29,577  dev gateleen INFO request - %WFk9 PUT /gateleen/server/event/v1/foo/bar s=service ";

        RecordParser recordParser = new RecordParser(pattern, Pattern.compile("(?<target>.*)"), timeFormat);

        Record record = recordParser.parse(data);

        assertNotNull(record);

        if(record != null) {
            assertEquals("PUT", record.getHttpMethod());
            assertEquals("/gateleen/server/event/v1/foo/bar", record.getUrl());
            assertEquals("/gateleen/server/event/v1/foo/bar", record.getRecipient());
            assertEquals("service", record.getSender());
            assertEquals(LocalDateTime.of(2017, 10, 20, 9, 11, 29, 577000000), record.getTime());
        }
    }

    @Test
    void batchParse() {
        String data = "2017-10-20 09:11:29,577  dev gateleen INFO request - %WFk9 PUT /gateleen/server/event/v1/foo/bar s=service " + System.lineSeparator() +
                "2017-10-20 09:11:29,577  dev gateleen INFO request - %WFk9 PUT /gateleen/server/event/v1/foo/bar s=service " + System.lineSeparator() +
                "2017-10-20 09:11:29,577  dev gateleen INFO request - %WFk9 PUT /gateleen/server/event/v1/foo/bar s=service ";

        RecordParser recordParser = new RecordParser(pattern,  Pattern.compile("(?<target>.*)"), timeFormat);

        List<Record> records = recordParser.batchParse(data);

        assertEquals(3, records.size());



        if(records.size() > 0) {
            Record record = records.get(0);
            assertEquals("PUT", record.getHttpMethod());
            assertEquals("/gateleen/server/event/v1/foo/bar", record.getUrl());
            assertEquals("service", record.getSender());
            assertEquals(LocalDateTime.of(2017, 10, 20, 9, 11, 29, 577000000), record.getTime());
        }
    }
}