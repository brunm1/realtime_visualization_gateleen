import ch.bfh.ti.gapa.integration.model.GapaMessage;
import ch.bfh.ti.gapa.integration.server.converter.GapaMessageToJsonConverter;
import ch.bfh.ti.gapa.integration.server.converter.JsonSender;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.ServerWebSocket;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicReference;

public class Server {

    public static void main(String[] args) {
        //Create Vertx instance
        Vertx vertx = Vertx.vertx();

        //create http server with vertx
        HttpServer httpServer = vertx.createHttpServer();

        //Add websocket handler
        AtomicReference<ServerWebSocket> webSocketClient = new AtomicReference<>();
        httpServer.websocketHandler(webSocket->{
            System.out.println("Gapa connects over websocket");
            webSocketClient.set(webSocket);
        });

        //send request to Gapa
        httpServer.requestHandler(httpServerRequest -> {
            System.out.println("Server received request: " + httpServerRequest.absoluteURI());

            JsonSender jsonSender = jsonObject -> {
                if(webSocketClient.get() == null) {
                    System.out.println("There is no Gapa instance connected. Dropping request");
                } else {
                    webSocketClient.get().writeFinalTextFrame(jsonObject.toString());
                }
            };
            GapaMessageToJsonConverter gapaMessageToJsonConverter = new GapaMessageToJsonConverter(jsonSender);

            GapaMessage gapaMessage = new GapaMessage();
            gapaMessage.setMethod(GapaMessage.Method.valueOf(httpServerRequest.method().name()));
            gapaMessage.setPeer(httpServerRequest.getHeader("service"));
            gapaMessage.setType(GapaMessage.Type.inbound);
            gapaMessage.setPath(httpServerRequest.path());
            gapaMessage.setTimestamp(Instant.now());

            System.out.println("Server sends received request to Gapa");

            gapaMessageToJsonConverter.sendGapaMessage(gapaMessage);
            httpServerRequest.response().end();
        });

        //start http server
        httpServer.listen(8081);
    }
}
