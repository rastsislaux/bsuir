package org.example;

import net.ostis.jesc.agent.ScAgentRegistry;
import net.ostis.jesc.api.ScApi;
import net.ostis.jesc.client.ScClient;
import net.ostis.jesc.client.model.element.ScEventType;
import net.ostis.jesc.context.ScContextCommon;

import java.util.Set;

public class Main {
    public static void main(String[] args) {
        var client = new ScClient("localhost", 8090);
        var agentRegistry = new ScAgentRegistry(client);
        var context = new ScContextCommon(new ScApi(client));

        var questionNode = context.findBySystemIdentifier("v3_qfis").get();
        var eventId = context.createEvent(ScEventType.ADD_OUTGOING_EDGE, questionNode);

        agentRegistry.registerAgent(new IsomorphicSubgraphSearchAgent(Set.of(eventId), client));
        while (true) { }
    }
}