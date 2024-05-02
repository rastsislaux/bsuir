package net.ostis.jesc.agent;

import lombok.RequiredArgsConstructor;
import net.ostis.jesc.client.ScClient;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class ScAgentRegistry {

    private final ScClient scClient;

    public void registerAgent(ScAgent agent) {
        log.info("Registering agent {}", agent.getClass().getName());

        scClient.addEventHandler(event -> {
            if (!agent.getTriggerEventIds().contains(event.getId())) {
                return;
            }

            agent.onTrigger(event);
        });
        
    }

    public void registerAgents(ScAgent... agents) {
        for (ScAgent agent : agents) {
            registerAgent(agent);
        }
    }

}
