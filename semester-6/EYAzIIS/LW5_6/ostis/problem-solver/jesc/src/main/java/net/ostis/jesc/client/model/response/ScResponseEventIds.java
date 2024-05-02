package net.ostis.jesc.client.model.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.ostis.jesc.client.model.response.payload.ResponsePayloadList;
import net.ostis.jesc.client.model.response.payload.entry.EventIdPayloadEntry;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ScResponseEventIds extends ScResponse<ResponsePayloadList<EventIdPayloadEntry>> {
}
