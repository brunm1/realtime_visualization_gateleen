package ch.bfh.ti.gapa.cli;

import ch.bfh.ti.gapa.domain.recording.Record;
import ch.bfh.ti.gapa.process.diagram.SequenceDiagramGenerator;
import ch.bfh.ti.gapa.process.reader.StringFromInputStreamReader;
import ch.bfh.ti.gapa.process.recording.RecordParser;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class Cli {
    public static void main(String[] args) throws IOException {
        String logPatternArg = args[0];
        String timeFormat = args[1];
        InputStream is = System.in;
        RecordParser recordParser =
                new RecordParser(Pattern.compile(logPatternArg),Pattern.compile("(?<target>.*)"), DateTimeFormatter.ofPattern(timeFormat, Locale.GERMANY));
        String log = StringFromInputStreamReader.readStringFromInputStream(is, Charset.forName("UTF-8"), 1024);
        List<Record> records = recordParser.batchParse(log);
        String plantUmlDiagram = new SequenceDiagramGenerator().generatePlantUmlSequenceDiagramFromRecords(records);
        System.out.println(plantUmlDiagram);
    }


}
