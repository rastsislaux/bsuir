package ostis.legislation.agent

import net.ostis.jesc.agent.ScAgent
import net.ostis.jesc.client.ScClient
import net.ostis.jesc.client.model.element.ScReference
import net.ostis.jesc.client.model.response.ScEvent
import net.ostis.jesc.client.model.type.ScType
import ostis.legislation.StringSearch
import ostis.legislation.extension.getElements
import ostis.legislation.extension.getRoleRelationTarget

class WhatIsAgent(event: Long, client: ScClient): ScAgent(setOf(event), client) {

    private val rrelIntent =
        context.resolveBySystemIdentifier("rrel_intent", ScType.NODE_CONST_ROLE)
    private val rrelFirstArgument =
        context.resolveBySystemIdentifier("rrel_first_argument", ScType.NODE_CONST_ROLE)
    private val rrelAnswerNaturalLang =
        context.resolveBySystemIdentifier("rrel_answer_natural_lang", ScType.NODE_CONST_ROLE)

    private val intentWhatIs =
        context.resolveBySystemIdentifier("intent_what_is", ScType.NODE_CONST)

    private val legalTermAddr = context.findBySystemIdentifier("belarus_legal_term").get()
    private val nrelScTextTranslation = context.findBySystemIdentifier("nrel_sc_text_translation").get()
    private val definitionAddr = context.findBySystemIdentifier("definition").get()

    override fun onTrigger(event: ScEvent) {
        val struct = event.payload[2]

        val intentAddr = context.getRoleRelationTarget(struct, rrelIntent)
        if (intentAddr != intentWhatIs) {
            return
        }

        val arg = context.getRoleRelationTarget(struct, rrelFirstArgument).let { context.getLinkContent(it) }.asText()

        val termAddrs = context.getElements(legalTermAddr)

        val idtfs = termAddrs.map { context.getMainIdentifier(it, "lang_ru") }
        val idtf = StringSearch.findMostSimilarString(arg, idtfs)

        val termAddr = termAddrs[idtfs.indexOf(idtf)]

        val linkAddr = context.api.searchByTemplate()
            .references(
                ScReference.type(ScType.VAR, "definition_node"),
                ScReference.type(ScType.EDGE_ACCESS_VAR_POS_PERM),
                ScReference.addr(termAddr)
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

        val answerRelation = context.createEdge(ScType.EDGE_ACCESS_CONST_POS_PERM, struct, linkAddr)
        context.createEdge(ScType.EDGE_ACCESS_CONST_POS_PERM, rrelAnswerNaturalLang, answerRelation)
    }

}
