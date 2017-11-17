package ch.bfh.ti.gapa.process.interfaces;

import ch.bfh.ti.gapa.domain.recording.Record;
import ch.bfh.ti.gapa.process.diagram.SequenceDiagramGenerator;
import ch.bfh.ti.gapa.process.reader.StringFromInputStreamReader;
import ch.bfh.ti.gapa.process.recording.RecordParser;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.regex.Pattern;

public class ProcessLayerImpl implements ProcessLayer{
    @Override
    public String process(Input input) throws IOException {
        //parse logs and produce records
        RecordParser recordParser =
                new RecordParser(input.getInboundRequestPattern(),
                                /*TODO change/remove */ Pattern.compile("(?<target>.*)"),
                        input.getDateTimeFormatter());
        String log = StringFromInputStreamReader.readStringFromInputStream(input.getInputStream(), Charset.forName("UTF-8"), 1024);
        List<Record> records = recordParser.batchParse(log);

        //process records and output plantUml diagram
        return new SequenceDiagramGenerator().generatePlantUmlSequenceDiagramFromRecords(records);
    }
}
