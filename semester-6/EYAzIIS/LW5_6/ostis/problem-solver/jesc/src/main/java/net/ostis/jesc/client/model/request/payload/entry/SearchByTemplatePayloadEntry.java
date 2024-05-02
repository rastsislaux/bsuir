package net.ostis.jesc.client.model.request.payload.entry;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import net.ostis.jesc.client.model.element.ScReference;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SearchByTemplatePayloadEntry extends ArrayList<ScReference> implements ScRequestPayloadEntry {

    private SearchByTemplatePayloadEntry(List<ScReference> scReferences) {
        super(scReferences);
    }

    public static SearchByTemplatePayloadEntry of(ScReference... scReferences) {
        return new SearchByTemplatePayloadEntry(List.of(scReferences));
    }

}
