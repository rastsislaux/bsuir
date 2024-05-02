package net.ostis.jesc.client.model.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ScResponse<T> {

    private Long id;

    private Boolean status;

    private Boolean event;

    private T payload;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<String> errors;

}
