package net.ostis.jesc.client.model.element;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ElementType {

    NODE("node"),
    EDGE("edge"),
    LINK("link");

    @JsonValue
    public final String value;

}
