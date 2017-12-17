package ch.bfh.ti.gapa.process.interfaces;

import ch.bfh.ti.gapa.domain.recording.Record;
import ch.bfh.ti.gapa.integration.client.converter.JsonToGapaMessageConverter;
import ch.bfh.ti.gapa.integration.client.socket.GapaWebSocketClient;
import ch.bfh.ti.gapa.integration.client.socket.GapaWebSocketClientHandler;
import ch.bfh.ti.gapa.integration.client.socket.StringReceiver;
import ch.bfh.ti.gapa.integration.client.validation.GapaMessageJsonValidator;
import ch.bfh.ti.gapa.integration.client.validation.JsonReceiver;
import ch.bfh.ti.gapa.integration.model.GapaMessage;
import ch.bfh.ti.gapa.process.AsyncTaskHandler;
import ch.bfh.ti.gapa.process.diagram.SequenceDiagramGenerator;
import ch.bfh.ti.gapa.process.filter.FilterConverter;
import ch.bfh.ti.gapa.process.recording.GapaMessageRecorder;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ProcessLayerImpl implements ProcessLayer{
    private static final Logger LOGGER = Logger.getLogger(ProcessLayerImpl.class.getName());

    private Input input;
    private GapaMessageRecorder gapaMessageRecorder;
    private GapaWebSocketClient gapaWebSocketClient;
    private AsyncTaskHandler<String> asyncHandler;

    private List<Record> convertMessagesToRecords() {
        //convert messages to domain model used by process logic
        return gapaMessageRecorder.getGapaMessages().stream().map(gapaMessage -> {
            LOGGER.info(gapaMessage.toString());
            Record record = new Record();
            record.setHttpMethod(gapaMessage.getMethod().name());
            if (gapaMessage.getType() == GapaMessage.Type.inbound) {
                record.setRecipient(input.getServerName());
                record.setSender(gapaMessage.getPeer());
            } else {
                record.setSender(input.getServerName());
                record.setRecipient(gapaMessage.getPeer());
            }
            record.setTime(LocalDateTime.ofInstant(gapaMessage.getTimestamp(), ZoneOffset.UTC));
            record.setUrl(gapaMessage.getPath());
            return record;
        }).collect(Collectors.toList());
    }

    private void generatePlantUml() {
        List<Record> records = convertMessagesToRecords();

        //apply filters
        if (input.getFilters() != null) {
            List<Predicate<Record>> filters = FilterConverter.convert(input.getFilters());
            Predicate<Record> theOneFilter = filters.stream().reduce(Predicate::and).orElse(x -> true);
            records = records.stream().filter(theOneFilter).collect(Collectors.toList());
        }

        //process records and output plantUml diagram
        String plantUml = new SequenceDiagramGenerator().generatePlantUmlSequenceDiagramFromRecords(records);
        this.asyncHandler.onResult(plantUml);
    }

    @Override
    public void stopRecording() {
        gapaWebSocketClient.close();
    }

    @Override
    public void run(Input input, AsyncTaskHandler<String> asyncHandler) {
        this.input = input;
        this.asyncHandler = asyncHandler;

        gapaMessageRecorder = new GapaMessageRecorder();
        JsonReceiver jsonReceiver = new JsonToGapaMessageConverter(gapaMessageRecorder);
        StringReceiver stringReceiver = new GapaMessageJsonValidator(jsonReceiver);
        GapaWebSocketClientHandler gapaWebSocketClientHandler = new GapaWebSocketClientHandler() {
            @Override
            public void onError(Exception ex) {
                asyncHandler.onError(ex);
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                //TODO differentiate between normal close and forced close by server
                LOGGER.info("Connection closed by " + ( remote ? "remote peer" : "us" ) + " Code: " + code + " Reason: " + reason );
                generatePlantUml();
            }
        };
        gapaWebSocketClient = new GapaWebSocketClient(input.getWebsocketUri(), stringReceiver, gapaWebSocketClientHandler);
        gapaWebSocketClient.connect();
    }
}
