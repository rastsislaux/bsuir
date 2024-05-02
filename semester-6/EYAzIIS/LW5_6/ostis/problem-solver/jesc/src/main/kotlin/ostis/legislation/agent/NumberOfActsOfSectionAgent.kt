package ostis.legislation.agent;

import net.ostis.jesc.agent.ScAgent
import net.ostis.jesc.client.ScClient
import net.ostis.jesc.client.model.element.ScContentType
import net.ostis.jesc.client.model.element.ScReference
import net.ostis.jesc.client.model.response.ScEvent
import net.ostis.jesc.client.model.type.ScType
import ostis.legislation.StringSearch
import ostis.legislation.extension.*

public class NumberOfActsOfSectionAgent(
event: Long,
client: ScClient
): ScAgent(setOf(event), client) {

    private val rrelIntent =
        context.resolveBySystemIdentifier("rrel_intent", ScType.NODE_CONST_ROLE)
    private val rrelFirstArgument =
        context.resolveBySystemIdentifier("rrel_first_argument", ScType.NODE_CONST_ROLE)
    private val rrelAnswerNaturalLang =
        context.resolveBySystemIdentifier("rrel_answer_natural_lang", ScType.NODE_CONST_ROLE)
    private val belarusLegalActAddr = context.findBySystemIdentifier("belarus_legal_act").get()

    private val intentAllActsSearch =
        context.resolveBySystemIdentifier("intent_all_acts_search", ScType.NODE_CONST)


    private val nrelArticle = context.findBySystemIdentifier("nrel_article").get()

    override fun onTrigger(event: ScEvent) {

        val struct = event.payload[2]

        val intentAddr = context.getRoleRelationTarget(struct, rrelIntent)
        if (intentAddr != intentAllActsSearch) {
            return
        }

        val arg = context.getRoleRelationTarget(struct, rrelFirstArgument).let { context.getLinkContent(it) }.asText()

        val termAddrs = context.getElements(belarusLegalActAddr)

        val idtfs = termAddrs.map { context.getMainIdentifier(it, "lang_ru") }
        val idtf = StringSearch.findMostSimilarString(arg, idtfs)
        val termAddr = termAddrs[idtfs.indexOf(idtf)]

        val answer: String = context.getRelationTargets(termAddr, nrelArticle, ScType.EDGE_D_COMMON_VAR).size.toString()

        context.api.createElements()
            .link(ScType.LINK_CONST, answer, ScContentType.STRING)
            .edge(ScType.EDGE_ACCESS_CONST_POS_PERM, ScReference.addr(struct), ScReference.ref(0))
            .edge(ScType.EDGE_ACCESS_CONST_POS_PERM, ScReference.addr(rrelAnswerNaturalLang), ScReference.ref(1))
            .execute()

    }

}
