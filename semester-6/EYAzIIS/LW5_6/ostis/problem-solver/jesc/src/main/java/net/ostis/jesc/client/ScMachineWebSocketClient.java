package net.ostis.jesc.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.ostis.jesc.client.model.response.ScEvent;
import net.ostis.jesc.client.model.response.ScResponse;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.SynchronousQueue;
import java.util.function.Consumer;

@Slf4j
public class ScMachineWebSocketClient extends WebSocketClient {

    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private Map<Long, String> messages = new ConcurrentHashMap<>();

    public List<Consumer<ScEvent>> eventHandlers = new ArrayList<>();

    @SneakyThrows
    public String getMessage(Long id) {
        var message = messages.get(id);
        while (message == null) {
            message = messages.get(id);
        }
        return message;
    }

    @Builder
    public ScMachineWebSocketClient(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake handshakeData) {

    }

    @Override
    @SneakyThrows
    public void onMessage(String message) {
        log.debug("Received from SC-machine: {}", message);

        if (message.contains("\"event\":1")) {
            eventHandlers.forEach(handler -> {
                new Thread(() -> {
                    try {
                        handler.accept(objectMapper.readValue(message, ScEvent.class));
                    } catch (JsonProcessingException e) { throw new RuntimeException(e); }
                }).start();
            });
            return;
        }

        var id = objectMapper.readValue(message, ScResponse.class).getId();
        messages.put(id, message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {

    }

    @Override
    public void onError(Exception ex) {

    }
}
