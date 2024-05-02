package ostis.legislation.agent

import net.ostis.jesc.agent.ScAgent
import net.ostis.jesc.client.ScClient
import net.ostis.jesc.client.model.element.ScContentType
import net.ostis.jesc.client.model.element.ScReference
import net.ostis.jesc.client.model.response.ScEvent
import net.ostis.jesc.client.model.type.ScType
import ostis.legislation.StringSearch
import ostis.legislation.extension.*

class FindArticlesAgent(event: Long, client: ScClient): ScAgent(setOf(event), client) {

    private val rrelIntent =
        context.resolveBySystemIdentifier("rrel_intent", ScType.NODE_CONST_ROLE)
    private val rrelFirstArgument =
        context.resolveBySystemIdentifier("rrel_first_argument", ScType.NODE_CONST_ROLE)
    private val rrelAnswerNaturalLang =
        context.resolveBySystemIdentifier("rrel_answer_natural_lang", ScType.NODE_CONST_ROLE)

    private val nrelArticle =
        context.resolveBySystemIdentifier("nrel_article", ScType.NODE_CONST_NOROLE)

    private val intentFindArticles =
        context.resolveBySystemIdentifier("intent_find_articles", ScType.NODE_CONST)

    private val legalActAddr = context.findBySystemIdentifier("legal_act").get()

    override fun onTrigger(event: ScEvent) {
        val struct = event.payload[2]

        val intentAddr = context.getRoleRelationTarget(struct, rrelIntent)
        if (intentAddr != intentFindArticles) {
            return
        }

        val codex = context.getRoleRelationTarget(struct, rrelFirstArgument).let { context.getLinkContent(it) }.asText()

        val termAddrs = context.getElements(legalActAddr)

        val idtfs = termAddrs.map { context.getMainIdentifier(it, "lang_ru") }
        val idtf = StringSearch.findMostSimilarString(codex, idtfs)

        val codexAddr = termAddrs[idtfs.indexOf(idtf)]

        val articleAddrs = context.getNoRoleRelationTargets(codexAddr, nrelArticle)
        val articleIdtfs = articleAddrs
            .map { context.getMainIdentifier(it, "lang_ru") }
            .map { Regex("\\d+").find(it)?.value?.toIntOrNull() to it }
            .sortedBy { it.first }
            .map { it.second }

        val answer = articleIdtfs.joinToString(", \n")

        val answerAddr = context.api.createElements()
            .link(ScType.LINK_CONST, answer, ScContentType.STRING)
            .execute().payload[0].scAddr

        val answerRelation = context.createEdge(ScType.EDGE_ACCESS_CONST_POS_PERM, struct, answerAddr)
        context.createEdge(ScType.EDGE_ACCESS_CONST_POS_PERM, rrelAnswerNaturalLang, answerRelation)
    }

}
