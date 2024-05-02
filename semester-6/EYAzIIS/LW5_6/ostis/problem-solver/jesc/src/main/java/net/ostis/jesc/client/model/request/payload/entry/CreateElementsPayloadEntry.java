package net.ostis.jesc.client.model.request.payload.entry;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import net.ostis.jesc.client.model.element.ElementType;
import net.ostis.jesc.client.model.element.ScContentType;
import net.ostis.jesc.client.model.element.ScReference;
import net.ostis.jesc.client.model.type.ScType;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateElementsPayloadEntry implements ScRequestPayloadEntry {

    private ElementType el;
    private ScType type;
    private ScReference src;
    private ScReference trg;
    private Object content;
    private ScContentType contentType;

    public static CreateElementsPayloadEntry node(ScType nodeType) {
        return CreateElementsPayloadEntry.builder()
                .el(ElementType.NODE)
                .type(nodeType)
                .build();
    }

    public static CreateElementsPayloadEntry edge(ScType edgeType, ScReference source, ScReference target) {
        return CreateElementsPayloadEntry.builder()
                .el(ElementType.EDGE)
                .src(source)
                .trg(target)
                .type(edgeType)
                .build();
    }

    public static CreateElementsPayloadEntry link(ScType linkType, Object content, ScContentType contentType) {
        return CreateElementsPayloadEntry.builder()
                .el(ElementType.LINK)
                .type(linkType)
                .content(content)
                .contentType(contentType)
                .build();
    }

}
