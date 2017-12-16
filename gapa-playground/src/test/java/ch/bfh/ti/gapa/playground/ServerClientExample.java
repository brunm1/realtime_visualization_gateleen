package ch.bfh.ti.gapa.playground;

import ch.bfh.ti.gapa.cli.CliImpl;
import ch.bfh.ti.gapa.cli.InputSupplier;
import ch.bfh.ti.gapa.integration.model.GapaMessage;
import ch.bfh.ti.gapa.integration.server.converter.GapaMessageToJsonConverter;
import ch.bfh.ti.gapa.integration.server.converter.JsonSender;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.ServerWebSocket;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicReference;

public class ServerClientExample {

    @Test
    public void start() {

        //SERVER CODE (GATELEEN)------------------------------------
        //Create Vertx instance
        Vertx vertx = Vertx.vertx();

        //create http server with vertx
        HttpServer httpServer = vertx.createHttpServer();

        //Add websocket handler
        AtomicReference<ServerWebSocket> webSocketClient = new AtomicReference<>();
        httpServer.websocketHandler(webSocketClient::set);

        //send request to Gapa
        httpServer.requestHandler(httpServerRequest -> {
            System.out.println("server received dummy request");

            JsonSender jsonSender = jsonObject -> webSocketClient.get().writeFinalTextFrame(jsonObject.toString());
            GapaMessageToJsonConverter gapaMessageToJsonConverter = new GapaMessageToJsonConverter(jsonSender);

            GapaMessage gapaMessage = new GapaMessage();
            gapaMessage.setMethod(GapaMessage.Method.valueOf(httpServerRequest.method().name()));
            gapaMessage.setPeer(httpServerRequest.getHeader("service"));
            gapaMessage.setType(GapaMessage.Type.inbound);
            gapaMessage.setPath(httpServerRequest.path());
            gapaMessage.setTimestamp(Instant.now());

            gapaMessageToJsonConverter.sendGapaMessage(gapaMessage);
        });

        //start http server
        httpServer.listen(7020);

        //CLIENT CODE (GAPA)------------------------------------------
        new Thread(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            CliImpl cliImpl = new CliImpl();

            InputSupplier inputSupplierMock = () -> {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return "";
            };
            cliImpl.setInputSupplier(inputSupplierMock);

            System.out.println("Start cliImpl with no arguments");
            int exitCode = cliImpl.run(new String[]{"-w", "http://localhost:7020", "-c", "/home/marc/Documents/visualization_gateleen/gapa-playground/src/main/resources/sample-config.json"});

            System.out.println("CliImpl exited with " + exitCode);
        }).start();


        //DUMMY REQUESTS TO GATELEEN (WILL BE RECORDED BY GAPA)-----------------
        new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            HttpClient client = Vertx.vertx().createHttpClient();
            HttpClientRequest httpClientRequest = client.put(7020, "localhost", "/gateleen/server/events", h -> {
                System.out.println("Sent dummy request to server");
            });
            httpClientRequest.putHeader("service", "jupiter");
            httpClientRequest.end("{ \"foo\": \"bar\" }");

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
