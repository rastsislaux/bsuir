package net.ostis.jesc.agent;

import lombok.Getter;
import net.ostis.jesc.api.ScApi;
import net.ostis.jesc.client.ScClient;
import net.ostis.jesc.client.model.response.ScEvent;
import net.ostis.jesc.context.ScContext;
import net.ostis.jesc.context.ScContextCommon;

import java.util.Set;

public abstract class ScAgent {

    protected final ScClient client;

    protected final ScApi api;

    protected final ScContext context;

    @Getter
    private final Set<Long> triggerEventIds;

    protected ScAgent(Set<Long> triggerEventIds, ScClient client) {
        this.client = client;
        this.api = new ScApi(client);
        this.context = new ScContextCommon(api);
        this.triggerEventIds = triggerEventIds;
    }

    protected ScAgent(Set<Long> triggerEventIds, ScClient client, ScApi api) {
        this.client = client;
        this.api = api;
        this.context = new ScContextCommon(api);
        this.triggerEventIds = triggerEventIds;
    }

    protected ScAgent(Set<Long> triggerEventIds, ScClient client, ScApi api, ScContext context) {
        this.client = client;
        this.api = api;
        this.context = context;
        this.triggerEventIds = triggerEventIds;
    }

    public abstract void onTrigger(ScEvent event);

}
