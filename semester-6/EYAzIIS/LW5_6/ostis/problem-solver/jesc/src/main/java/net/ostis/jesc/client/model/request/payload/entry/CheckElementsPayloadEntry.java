package net.ostis.jesc.client.model.request.payload.entry;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CheckElementsPayloadEntry implements ScRequestPayloadEntry {

    @JsonValue
    private final Long scAddr;

    public static CheckElementsPayloadEntry scAddr(Long scAddr) {
        return new CheckElementsPayloadEntry(scAddr);
    }

}
