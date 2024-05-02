package net.ostis.jesc.client.model.request;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ScRequestType {

    CREATE_ELEMENTS("create_elements"),
    CHECK_ELEMENTS("check_elements"),
    DELETE_ELEMENTS("delete_elements"),
    SEARCH_BY_TEMPLATE("search_template"),
    GENERATE_BY_TEMPLATE("generate_template"),
    EVENTS("events"),
    KEYNODES("keynodes"),
    CONTENT("content");

    @JsonValue
    final String value;

}
