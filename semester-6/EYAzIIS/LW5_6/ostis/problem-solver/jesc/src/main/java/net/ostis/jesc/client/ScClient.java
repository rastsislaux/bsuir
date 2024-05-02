package net.ostis.jesc.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.ostis.jesc.client.model.request.ScRequest;
import net.ostis.jesc.client.model.response.ScEvent;

import java.net.URI;
import java.util.function.Consumer;

/**
 * Communication with SC machine.
 *
 * Usage example:
 * <pre>
 * {@code
 * try (var client = new ScClient("localhost", 8090) {
 *     var api = new ScApi(scClient);
 *     ...
 * }
 * }
 * </pre>
 */
@Slf4j
public class ScClient implements AutoCloseable {

    private final ScMachineWebSocketClient wsc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @SneakyThrows
    public ScClient(String host, int port) {
        var wsUrl = String.format("ws://%s:%d/ws_json", host, port);
        wsc = new ScMachineWebSocketClient(
                new URI(wsUrl)
        );
        wsc.connectBlocking();
    }

    @SneakyThrows
    public ScClient(ScMachineWebSocketClient wsc) {
        this.wsc = wsc;
        wsc.connectBlocking();
    }

    @SneakyThrows
    public <T> T sendRequest(ScRequest scRequest, Class<T> responseType) {
        var rawRequest = objectMapper.writeValueAsString(scRequest);
        log.debug("Sent to SC-machine: {}", rawRequest);
        wsc.send(rawRequest);
        var rawResponse = wsc.getMessage(scRequest.getId());
        return objectMapper.readValue(rawResponse, responseType);
    }

    public void addEventHandler(Consumer<ScEvent> eventHandler) {
        wsc.eventHandlers.add(eventHandler);
    }

    public ScMachineWebSocketClient getWebSocketClient() {
        return wsc;
    }

    @Override
    @SneakyThrows
    public void close() {
        wsc.close();
    }
}
