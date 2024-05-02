package ostis.legislation.agent

import net.ostis.jesc.agent.ScAgent
import net.ostis.jesc.client.ScClient
import net.ostis.jesc.client.model.element.ScContentType
import net.ostis.jesc.client.model.element.ScReference
import net.ostis.jesc.client.model.response.ScEvent
import net.ostis.jesc.client.model.type.ScType
import ostis.legislation.extension.getElements
import ostis.legislation.extension.getRoleRelationTarget

class FirstLetterSearchAgent(
    event: Long,
    client: ScClient
): ScAgent(setOf(event), client) {

    private val rrelFirstArgument = context.resolveBySystemIdentifier(
        "rrel_first_argument",
        ScType.NODE_ROLE
    )

    private val belarusLegalTerm = context.resolveBySystemIdentifier(
        "belarus_legal_term",
        ScType.NODE_CLASS
    )

    private val rrelAnswer = context.resolveBySystemIdentifier(
        "rrel_answer_natural_lang",
        ScType.NODE_ROLE
    )

    override fun onTrigger(event: ScEvent) {
        val structAddr = event.payload[2]

        val paramAddr = context.getRoleRelationTarget(structAddr, rrelFirstArgument)
        val param = context.getLinkContent(paramAddr).asText()

        val answer = context.getElements(belarusLegalTerm)
            .map { context.getMainIdentifier(it, "lang_ru") }
            .filter { it.lowercase().startsWith(param.lowercase()) }
            .joinToString(", ")

        context.api.createElements()
            .link(ScType.LINK_CONST, answer, ScContentType.STRING)
            .edge(ScType.EDGE_ACCESS_CONST_POS_PERM, ScReference.addr(structAddr), ScReference.ref(0))
            .edge(ScType.EDGE_ACCESS_CONST_POS_PERM, ScReference.addr(rrelAnswer), ScReference.ref(1))
            .execute()
    }

}