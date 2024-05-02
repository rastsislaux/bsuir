package ostis.legislation.agent

import net.ostis.jesc.agent.ScAgent
import net.ostis.jesc.client.ScClient
import net.ostis.jesc.client.model.element.ScContentType
import net.ostis.jesc.client.model.element.ScReference
import net.ostis.jesc.client.model.response.ScEvent
import net.ostis.jesc.client.model.type.ScType
import ostis.legislation.StringSearch
import ostis.legislation.extension.*

class ArticleContentAgent(event: Long, client: ScClient): ScAgent(setOf(event), client) {

    private val rrelIntent =
        context.resolveBySystemIdentifier("rrel_intent", ScType.NODE_CONST_ROLE)
    private val rrelFirstArgument =
        context.resolveBySystemIdentifier("rrel_first_argument", ScType.NODE_CONST_ROLE)
    private val rrelSecondArgument =
        context.resolveBySystemIdentifier("rrel_second_argument", ScType.NODE_CONST_ROLE)
    private val rrelAnswerNaturalLang =
        context.resolveBySystemIdentifier("rrel_answer_natural_lang", ScType.NODE_CONST_ROLE)

    private val nrelArticle =
        context.resolveBySystemIdentifier("nrel_article", ScType.NODE_CONST_NOROLE)

    private val intentArticleContent =
        context.resolveBySystemIdentifier("intent_article_content", ScType.NODE_CONST)

    private val legalActAddr = context.findBySystemIdentifier("legal_act").get()
    private val nrelScTextTranslation = context.findBySystemIdentifier("nrel_sc_text_translation").get()
    private val definitionAddr = context.findBySystemIdentifier("definition").get()

    override fun onTrigger(event: ScEvent) {
        val struct = event.payload[2]

        val intentAddr = context.getRoleRelationTarget(struct, rrelIntent)
        if (intentAddr != intentArticleContent) {
            return
        }

        val codex = context.getRoleRelationTarget(struct, rrelFirstArgument).let { context.getLinkContent(it) }.asText()
        val articleNumber = context.getRoleRelationTarget(struct, rrelSecondArgument).let { context.getLinkContent(it) }.asInt()

        val termAddrs = context.getElements(legalActAddr)

        val idtfs = termAddrs.map { context.getMainIdentifier(it, "lang_ru") }
        val idtf = StringSearch.findMostSimilarString(codex, idtfs)

        val codexAddr = termAddrs[idtfs.indexOf(idtf)]

        val articleAddrs = context.getNoRoleRelationTargets(codexAddr, nrelArticle)
        val articleIdtfs = articleAddrs.map { context.getMainIdentifier(it, "lang_ru") }

        val articleIdtf = articleIdtfs.find { Regex("\\d+").find(it)?.value?.toIntOrNull() == articleNumber }

        if (articleIdtf == null) {
            val answerAddr = context.api.createElements()
                .link(ScType.LINK_CONST, "К сожалению, найти искомую статью не удалось.", ScContentType.STRING)
                .execute()
                .payload[0].scAddr

            val answerRelation = context.createEdge(ScType.EDGE_ACCESS_CONST_POS_PERM, struct, answerAddr)
            context.createEdge(ScType.EDGE_ACCESS_CONST_POS_PERM, rrelAnswerNaturalLang, answerRelation)
            return
        }

        val articleAddr = articleAddrs[articleIdtfs.indexOf(articleIdtf)]

        val articleTitle = context.getMainIdentifier(articleAddr, "lang_ru")
        val linkAddr = context.api.searchByTemplate()
            .references(
                ScReference.type(ScType.VAR, "definition_node"),
                ScReference.type(ScType.EDGE_ACCESS_VAR_POS_PERM),
                ScReference.addr(articleAddr)
            )
            .references(
                ScReference.addr(definitionAddr),
                ScReference.type(ScType.EDGE_ACCESS_VAR_POS_PERM),
                ScReference.alias("defintion_node")
            )
            .references(
                ScReference.type(ScType.VAR, "translation_node"),
                ScReference.type(ScType.EDGE_D_COMMON_VAR, "tranlsation_edge"),
                ScReference.alias("definition_node")
            )
            .references(
                ScReference.addr(nrelScTextTranslation),
                ScReference.type(ScType.EDGE_ACCESS_VAR_POS_PERM),
                ScReference.alias("translation_edge")
            )
            .references(
                ScReference.alias("translation_node"),
                ScReference.type(ScType.EDGE_ACCESS_VAR_POS_PERM),
                ScReference.type(ScType.LINK_VAR, "link")
            )
            .execute().payload.addrs[0][14]

        val answerAddr = context.api.createElements()
            .link(ScType.LINK_CONST,
                "$articleTitle\n\n${context.getLinkContent(linkAddr).asText()}",
                ScContentType.STRING)
            .execute().payload[0].scAddr

        val answerRelation = context.createEdge(ScType.EDGE_ACCESS_CONST_POS_PERM, struct, answerAddr)
        context.createEdge(ScType.EDGE_ACCESS_CONST_POS_PERM, rrelAnswerNaturalLang, answerRelation)
    }

}
