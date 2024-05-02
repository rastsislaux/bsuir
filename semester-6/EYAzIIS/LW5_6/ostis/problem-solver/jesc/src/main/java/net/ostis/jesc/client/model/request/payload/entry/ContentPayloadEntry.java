package net.ostis.jesc.client.model.request.payload.entry;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import net.ostis.jesc.client.model.element.ScContentType;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContentPayloadEntry implements ScRequestPayloadEntry {

    private Command command;
    private ScContentType type;
    private Object data;
    private Long addr;

    public static ContentPayloadEntry get(Long addr) {
        return ContentPayloadEntry.builder()
                .command(Command.GET)
                .addr(addr)
                .build();
    }

    public static ContentPayloadEntry set(ScContentType type, Object data, Long addr) {
        return ContentPayloadEntry.builder()
                .command(Command.SET)
                .addr(addr)
                .type(type)
                .data(data)
                .build();
    }

    @RequiredArgsConstructor
    public enum Command {
        SET("set"),
        GET("get");

        @JsonValue
        private final String jsonValue;
    }

}
