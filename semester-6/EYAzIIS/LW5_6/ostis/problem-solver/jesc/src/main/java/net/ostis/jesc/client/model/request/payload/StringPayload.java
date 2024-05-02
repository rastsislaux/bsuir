package net.ostis.jesc.client.model.request.payload;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StringPayload implements ScPayload {

    @JsonValue
    private String value;

}
