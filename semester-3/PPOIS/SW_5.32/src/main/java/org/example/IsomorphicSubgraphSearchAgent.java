package org.example;

import com.google.common.collect.Collections2;
import net.ostis.jesc.agent.ScAgent;
import net.ostis.jesc.client.ScClient;
import net.ostis.jesc.client.model.element.ScReference;
import net.ostis.jesc.client.model.response.ScEvent;
import net.ostis.jesc.client.model.type.ScType;
import net.ostis.jesc.util.Triplet;

import java.util.*;

public class IsomorphicSubgraphSearchAgent extends ScAgent {

    private Long questionNode;

    private Long relTarget;

    private Long relPattern;

    protected IsomorphicSubgraphSearchAgent(Set<Long> triggerEventIds, ScClient client) {
        super(triggerEventIds, client);

        questionNode = context.findBySystemIdentifier("v3_qfis").get();
        relTarget = context.findBySystemIdentifier("v3_qfis_target'").get();
        relPattern = context.findBySystemIdentifier("v3_qfis_pattern'").get();
    }

    Set<Long> getEdgeSet(Long parentNode) {
        var edges = new HashSet<Long>();
        context.iterator3(
                ScReference.addr(parentNode),
                ScReference.type(ScType.EDGE_ACCESS_VAR_POS_PERM),
                ScReference.type(ScType.EDGE_ACCESS_VAR_POS_PERM)
        ).forEach(t -> edges.add(t.getThird()));
        return edges;
    }

    Long getGraphParentNode(Long problemNode, Collection<Long> edges) {
        Long patternGraph;
        for (var t : context.iterator3(
                ScReference.addr(problemNode),
                ScReference.type(ScType.EDGE_ACCESS_VAR_POS_PERM),
                ScReference.type(ScType.NODE_VAR)
        )) {
            if (edges.contains((t.getSecond()))) {
                return t.getThird();
            }
        }

        throw new RuntimeException("Graph parent node not found.");
    }

    Set<Long> getGraphNodeSet(Long graphParentNode) {
        var nodeSet = new HashSet<Long>();
        for (var t : context.iterator3(
                ScReference.addr(graphParentNode),
                ScReference.type(ScType.EDGE_ACCESS_VAR_POS_PERM),
                ScReference.type(ScType.NODE_VAR)
        )) {
            nodeSet.add(t.getThird());
        }
        return nodeSet;
    }

    boolean areAdjacent(Long nodeAddr1, Long nodeAddr2) {
        for (Triplet<Long> t : context.iterator3(
                ScReference.addr(nodeAddr1),
                ScReference.type(ScType.EDGE_U_COMMON_VAR),
                ScReference.addr(nodeAddr2)
        )) {
            return true;
        }

        for (Triplet<Long> t : context.iterator3(
                ScReference.addr(nodeAddr2),
                ScReference.type(ScType.EDGE_U_COMMON_VAR),
                ScReference.addr(nodeAddr1)
        )) {
            return true;
        }
        return false;
    }

    @Override
    public void onTrigger(ScEvent event) {
        var problemNode = event.getPayload().get(2);
        var targetEdges = getEdgeSet(relTarget);
        var patternEdges = getEdgeSet(relPattern);

        var pattern = getGraphParentNode(problemNode, patternEdges);
        var target = getGraphParentNode(problemNode, targetEdges);

        var patternNodeSet = getGraphNodeSet(pattern);
        var targetNodeSet = getGraphNodeSet(target);

        var patternNodeList = new ArrayList<>(patternNodeSet);

        var isomorphisms = new HashSet<Map<Long, Long>>();

        for (List<Long> lst : Collections2.permutations(targetNodeSet)) {

            var patternToTarget = new HashMap<Long, Long>();
            var targetToPattern = new HashMap<Long, Long>();

            for (int i = 0; i < patternNodeList.size(); i++) {
                patternToTarget.put(patternNodeList.get(i), lst.get(i));
                targetToPattern.put(lst.get(i), patternNodeList.get(i));
            }

            var fits = true;
            checking: for (Long p1 : patternNodeList) {
                for (Long p2 : patternNodeList) {
                    var edgeExistsInPattern = areAdjacent(p1, p2);
                    var edgeExistsInTarget = areAdjacent(patternToTarget.get(p1), patternToTarget.get(p2));
                    if (edgeExistsInPattern != edgeExistsInTarget) {
                        fits = false;
                        break checking;
                    }
                }
            }

            if (fits) {
                isomorphisms.add(patternToTarget);
            }
        }

        System.out.printf("Target node set: %s%n", targetNodeSet);
        System.out.printf("Pattern node set: %s%n", patternNodeSet);
        System.out.printf("Isomorphisms: %s%n", isomorphisms);
    }

}