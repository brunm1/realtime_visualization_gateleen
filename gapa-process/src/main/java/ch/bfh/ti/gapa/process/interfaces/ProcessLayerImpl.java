package ch.bfh.ti.gapa.process.interfaces;

import ch.bfh.ti.gapa.domain.recording.Record;
import ch.bfh.ti.gapa.integration.client.Client;
import ch.bfh.ti.gapa.integration.client.converter.JsonToGapaMessageConverter;
import ch.bfh.ti.gapa.integration.client.socket.GapaWebSocketClient;
import ch.bfh.ti.gapa.integration.client.socket.StringReceiver;
import ch.bfh.ti.gapa.integration.client.validation.GapaMessageJsonValidator;
import ch.bfh.ti.gapa.integration.client.validation.JsonReceiver;
import ch.bfh.ti.gapa.integration.model.GapaMessage;
import ch.bfh.ti.gapa.process.diagram.SequenceDiagramGenerator;
import ch.bfh.ti.gapa.process.filter.FilterConverter;
import ch.bfh.ti.gapa.process.recording.GapaMessageRecorder;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ProcessLayerImpl implements ProcessLayer{
    @Override
    public String process(Input input) throws IOException {
        GapaMessageRecorder gapaMessageRecorder = new GapaMessageRecorder();
        JsonReceiver jsonReceiver = new JsonToGapaMessageConverter(gapaMessageRecorder);
        StringReceiver stringReceiver = new GapaMessageJsonValidator(jsonReceiver);
        Client gapaWebsocketClient =
                null;
        gapaWebsocketClient = new GapaWebSocketClient(input.getWebsocketUri(), stringReceiver);

        Client finalGapaWebsocketClient = gapaWebsocketClient;
        Thread t = new Thread(() -> {
                finalGapaWebsocketClient.connect();
        });
        t.start();

        //wait until user presses a key
        System.in.read();

        //TODO close connection

        List<Record> records = gapaMessageRecorder.getGapaMessages().stream().map(gapaMessage -> {
            Record record = new Record();
            record.setHttpMethod(gapaMessage.getMethod().name());
            if(gapaMessage.getType() == GapaMessage.Type.inbound) {
                record.setRecipient("gateleen");
                record.setSender(gapaMessage.getPeer());
            } else {
                record.setSender("gateleen");
                record.setRecipient(gapaMessage.getPeer());
            }
            record.setTime(LocalDateTime.ofInstant(gapaMessage.getTimestamp(), ZoneOffset.UTC));
            record.setUrl(gapaMessage.getPath());
            return record;
        }).collect(Collectors.toList());


        //filter
        List<Predicate<Record>> filters = FilterConverter.convert(input.getFilters());
        Predicate<Record> theOneFilter=filters.stream().reduce(Predicate::and).orElse(x->true);
        List<Record> filteredRecords = records.stream().filter(theOneFilter).collect(Collectors.toList());

        //process records and output plantUml diagram
        return new SequenceDiagramGenerator().generatePlantUmlSequenceDiagramFromRecords(filteredRecords);
    }
}
