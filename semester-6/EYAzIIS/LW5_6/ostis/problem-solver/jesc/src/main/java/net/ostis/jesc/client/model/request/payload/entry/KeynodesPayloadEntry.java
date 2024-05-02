package net.ostis.jesc.client.model.request.payload.entry;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import net.ostis.jesc.client.model.type.ScType;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class KeynodesPayloadEntry implements ScRequestPayloadEntry {

    private Command command;

    private String idtf;

    @JsonProperty("elType")
    private ScType elType;

    public static KeynodesPayloadEntry find(String idtf) {
        return KeynodesPayloadEntry.builder()
                .command(Command.FIND)
                .idtf(idtf)
                .build();
    }

    public static KeynodesPayloadEntry resolve(String idtf, ScType elType) {
        return KeynodesPayloadEntry.builder()
                .command(Command.RESOLVE)
                .idtf(idtf)
                .elType(elType)
                .build();
    }

    @RequiredArgsConstructor
    private enum Command {
        FIND("find"),
        RESOLVE("resolve");

        @JsonValue
        private final String value;
    }

}
