package net.ostis.jesc.client.model.response.payload;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class SearchByTemplateResponsePayload implements ScResponsePayload {

    private List<List<Long>> addrs;

    private Map<String, Long> aliases;


}
