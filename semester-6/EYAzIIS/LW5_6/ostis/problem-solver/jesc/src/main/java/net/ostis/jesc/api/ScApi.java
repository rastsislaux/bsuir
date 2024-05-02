package net.ostis.jesc.api;

import net.ostis.jesc.client.ScClient;
import net.ostis.jesc.client.model.element.ScContentType;
import net.ostis.jesc.client.model.element.ScEventType;
import net.ostis.jesc.client.model.element.ScReference;
import net.ostis.jesc.client.model.request.ScRequest;
import net.ostis.jesc.client.model.request.ScRequestType;
import net.ostis.jesc.client.model.request.payload.EventsPayload;
import net.ostis.jesc.client.model.request.payload.ListPayload;
import net.ostis.jesc.client.model.request.payload.entry.*;
import net.ostis.jesc.client.model.response.ScContentResponse;
import net.ostis.jesc.client.model.response.ScResponseAddrs;
import net.ostis.jesc.client.model.response.ScResponseEventIds;
import net.ostis.jesc.client.model.response.ScSearchByTemplateResponse;
import net.ostis.jesc.client.model.type.ScType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ScApi {

    private final ScClient scClient;

    private static Long idCounter = 0L;

    public ScApi(ScClient scClient) {
        this.scClient = scClient;
    }

    private Long makeId() {
        return ++idCounter;
    }

    public ScClient getClient() {
        return scClient;
    }

    public CreateElementsBuilder createElements() {
        return new CreateElementsBuilder();
    }

    public CheckElementsBuilder checkElements() {
        return new CheckElementsBuilder();
    }

    public DeleteElementsBuilder deleteElements() {
        return new DeleteElementsBuilder();
    }

    public KeynodesBuilder keynodes() {
        return new KeynodesBuilder();
    }

    public SearchByTemplateBuilder searchByTemplate() {
        return new SearchByTemplateBuilder();
    }

    public ContentBuilder content() {
        return new ContentBuilder();
    }

    public EventsBuilder events() {
        return new EventsBuilder();
    }

    public class CreateElementsBuilder {

        private final List<CreateElementsPayloadEntry> entries = new ArrayList<>();

        private CreateElementsBuilder() { }

        public CreateElementsBuilder node(ScType nodeType) {
            this.entries.add(CreateElementsPayloadEntry.node(nodeType));
            return this;
        }

        public CreateElementsBuilder edge(ScType edgeType, ScReference source, ScReference target) {
            this.entries.add(CreateElementsPayloadEntry.edge(edgeType, source, target));
            return this;
        }

        public CreateElementsBuilder link(ScType linkType, Object content, ScContentType contentType) {
            this.entries.add(CreateElementsPayloadEntry.link(linkType, content, contentType));
            return this;
        }

        public ScResponseAddrs execute() {
            return scClient.sendRequest(new ScRequest<>(
                    makeId(), ScRequestType.CREATE_ELEMENTS,
                    ListPayload.of(entries.toArray(CreateElementsPayloadEntry[]::new))
            ), ScResponseAddrs.class);
        }

    }

    public class CheckElementsBuilder {

        private final List<CheckElementsPayloadEntry> entries = new ArrayList<>();

        private CheckElementsBuilder() { }

        public CheckElementsBuilder scAddr(Long scAddr) {
            entries.add(CheckElementsPayloadEntry.scAddr(scAddr));
            return this;
        }

        public ScResponseAddrs execute() {
            return scClient.sendRequest(new ScRequest<>(
                    makeId(), ScRequestType.CHECK_ELEMENTS,
                    ListPayload.of(entries.toArray(CheckElementsPayloadEntry[]::new))
            ), ScResponseAddrs.class);
        }

    }

    public class DeleteElementsBuilder {

        private final List<DeleteElementsPayloadEntry> entries = new ArrayList<>();

        private DeleteElementsBuilder() { }

        public DeleteElementsBuilder scAddr(Long scAddr) {
            entries.add(DeleteElementsPayloadEntry.scAddr(scAddr));
            return this;
        }

        public boolean execute() {
            return scClient.sendRequest(new ScRequest<>(
                    makeId(), ScRequestType.DELETE_ELEMENTS,
                    ListPayload.of(entries.toArray(DeleteElementsPayloadEntry[]::new))
            ), Boolean.class);
        }

    }

    public class ContentBuilder {

        private final List<ContentPayloadEntry> entries = new ArrayList<>();

        private ContentBuilder() { }

        public ContentBuilder get(Long addr) {
            entries.add(ContentPayloadEntry.get(addr));
            return this;
        }

        public ContentBuilder set(ScContentType contentType, Object data, Long addr) {
            entries.add(ContentPayloadEntry.set(contentType, data, addr));
            return this;
        }

        /**
         * Notice: Actual API method is not void.
         * It is decided to implement return class
         * later due to <b>ABSOLUTELY TERRIBLE</b> response from sc-machine.
         *
         * Example from SC-machine documentation:
         * <pre>{@code
         * {
         *   ..., // common response data
         *   "payload": [
         *     // List of command results
         *     true,      // true or false for a set command result
         *     // for get command it returns content with a type
         *     {
         *       "value": 56.7,  // value will be a null, if content doesn't exist
         *       "type": "float"
         *     },
         *     ... // other command results
         *   ]
         * }
         * }</pre>
         *
         * See how it returns booleans and objects in the same list?<br>
         * I just don't want to implement it...
         */
        public ScContentResponse execute() {
            return scClient.sendRequest(new ScRequest<>(
                    makeId(), ScRequestType.CONTENT,
                    ListPayload.of(entries.toArray(ContentPayloadEntry[]::new))
            ), ScContentResponse.class);
        }

    }

    public class KeynodesBuilder {

        private final List<KeynodesPayloadEntry> entries = new ArrayList<>();

        private KeynodesBuilder() { }

        public KeynodesBuilder find(String idtf) {
            entries.add(KeynodesPayloadEntry.find(idtf));
            return this;
        }

        public KeynodesBuilder resolve(String idtf, ScType scType) {
            entries.add(KeynodesPayloadEntry.resolve(idtf, scType));
            return this;
        }

        public ScResponseAddrs execute() {
            return scClient.sendRequest(new ScRequest<>(
                    makeId(), ScRequestType.KEYNODES,
                    ListPayload.of(entries.toArray(KeynodesPayloadEntry[]::new))
            ), ScResponseAddrs.class);
        }

    }

    public class EventsBuilder {

        private final List<EventsPayload.Create> createEntries = new ArrayList<>();

        private final List<Long> deleteEntries = new ArrayList<>();

        private EventsBuilder() { }

        public EventsBuilder create(ScEventType type, Long addr) {
            createEntries.add(EventsPayload.Create.event(type, addr));
            return this;
        }

        public EventsBuilder delete(Long eventId) {
            deleteEntries.add(eventId);
            return this;
        }

        public ScResponseEventIds execute() {
            return scClient.sendRequest(new ScRequest<>(
                    makeId(), ScRequestType.EVENTS,
                    new EventsPayload(
                            createEntries,
                            deleteEntries
                    )
            ), ScResponseEventIds.class);
        }

    }

    public class SearchByTemplateBuilder {

        private final List<SearchByTemplatePayloadEntry> entries = new ArrayList<>();

        private SearchByTemplateBuilder() { }

        public SearchByTemplateBuilder references(ScReference... scReferences) {
            entries.add(SearchByTemplatePayloadEntry.of(scReferences));
            return this;
        }

        public SearchByTemplateBuilder references(Collection<ScReference> scReferences) {
            entries.add(SearchByTemplatePayloadEntry.of(scReferences.toArray(ScReference[]::new)));
            return this;
        }

        public ScSearchByTemplateResponse execute() {
            return scClient.sendRequest(new ScRequest<>(
                    makeId(), ScRequestType.SEARCH_BY_TEMPLATE,
                    ListPayload.of(entries.toArray(SearchByTemplatePayloadEntry[]::new))
            ), ScSearchByTemplateResponse.class);
        }

    }

}
