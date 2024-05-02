package net.ostis.jesc.client.model.response;

import lombok.Data;

import java.util.List;

@Data
public class ScEvent {

    Long id;

    Boolean event;

    Boolean status;

    List<Long> payload;

}
