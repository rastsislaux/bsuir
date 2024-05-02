package net.ostis.jesc.client.model.element;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ScReferenceType {
    ADDR("addr"),
    REF("ref"),
    ALIAS("alias"),
    TYPE("type");

    @JsonValue
    final String value;
}
