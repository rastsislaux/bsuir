package net.ostis.jesc.client.model.request.payload;

import net.ostis.jesc.client.model.request.payload.entry.ScRequestPayloadEntry;

import java.util.ArrayList;
import java.util.List;

public class ListPayload extends ArrayList<ScRequestPayloadEntry> implements ScPayload {

    private ListPayload(List<ScRequestPayloadEntry> list) {
        super(list);
    }

    public static ListPayload of(ScRequestPayloadEntry... entries) {
        return new ListPayload(List.of(entries));
    }

}
