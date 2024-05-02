package net.ostis.jesc.client.model.element;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ScContentType {
    INT("int"),
    FLOAT("float"),
    STRING("string"),
    BINARY("binary");

    @JsonValue
    final String value;
}
