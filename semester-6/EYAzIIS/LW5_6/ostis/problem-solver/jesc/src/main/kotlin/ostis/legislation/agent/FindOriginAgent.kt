package ostis.legislation.agent

import net.ostis.jesc.agent.ScAgent
import net.ostis.jesc.client.ScClient
import net.ostis.jesc.client.model.element.ScContentType
import net.ostis.jesc.client.model.response.ScEvent
import net.ostis.jesc.client.model.type.ScType
import ostis.legislation.StringSearch
import ostis.legislation.extension.getElements
import ostis.legislation.extension.getNoRoleRelationTarget
import ostis.legislation.extension.getRoleRelationTarget

class FindOriginAgent(event: Long, client: ScClient): ScAgent(setOf(event), client) {

    private val rrelIntent =
        context.resolveBySystemIdentifier("rrel_intent", ScType.NODE_CONST_ROLE)
    private val rrelFirstArgument =
        context.resolveBySystemIdentifier("rrel_first_argument", ScType.NODE_CONST_ROLE)
    private val rrelAnswerNaturalLang =
        context.resolveBySystemIdentifier("rrel_answer_natural_lang", ScType.NODE_CONST_ROLE)

    private val intentFindOrigin =
        context.resolveBySystemIdentifier("intent_find_origin", ScType.NODE_CONST)

    private val legalTermAddr = context.findBySystemIdentifier("belarus_legal_term").get()
    private val nrelLegalAct = context.findBySystemIdentifier("nrel_legal_act").get()

    override fun onTrigger(event: ScEvent) {
        val struct = event.payload[2]

        val intentAddr = context.getRoleRelationTarget(struct, rrelIntent)
        if (intentAddr != intentFindOrigin) {
            return
        }

        val arg = context.getRoleRelationTarget(struct, rrelFirstArgument).let { context.getLinkContent(it) }.asText()

        val termAddrs = context.getElements(legalTermAddr)

        val idtfs = termAddrs.map { context.getMainIdentifier(it, "lang_ru") }
        val idtf = StringSearch.findMostSimilarString(arg, idtfs)

        val termAddr = termAddrs[idtfs.indexOf(idtf)]
        val documentAddr = context.getNoRoleRelationTarget(termAddr, nrelLegalAct)
        val documentIdtf = context.getMainIdentifier(documentAddr, "lang_ru")

        val linkAddr = context.api.createElements()
            .link(ScType.LINK_CONST, "Определение понятия «$idtf» дано в документе «$documentIdtf»", ScContentType.STRING)
            .execute().payload[0].scAddr

        val answerRelation = context.createEdge(ScType.EDGE_ACCESS_CONST_POS_PERM, struct, linkAddr)
        context.createEdge(ScType.EDGE_ACCESS_CONST_POS_PERM, rrelAnswerNaturalLang, answerRelation)
    }

}
