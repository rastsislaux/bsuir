package net.ostis.jesc.client.model.element;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ScEventType {

    ADD_OUTGOING_EDGE("add_outgoing_edge"),
    ADD_INGOING_EDGE("add_ingoing_edge"),
    REMOVE_OUTGOING_EDGE("remove_outgoing_edge"),
    REMOVE_INGOING_EDGE("remove_ingoing_edge"),
    CONTENT_CHANGE("content_change"),
    DELETE_ELEMENT("delete_element");

    @JsonValue
    private final String jsonValue;

}
