package ostis.legislation.agent

import net.ostis.jesc.agent.ScAgent
import net.ostis.jesc.client.ScClient
import net.ostis.jesc.client.model.element.ScContentType
import net.ostis.jesc.client.model.element.ScReference
import net.ostis.jesc.client.model.response.ScEvent
import net.ostis.jesc.client.model.type.ScType
import ostis.legislation.extension.*


class AllSectionsSearchAgent(event: Long, client: ScClient): ScAgent(setOf(event), client) {

    private val rrelIntent =
        context.resolveBySystemIdentifier("rrel_intent", ScType.NODE_CONST_ROLE)
    private val rrelAnswerNaturalLang =
        context.resolveBySystemIdentifier("rrel_answer_natural_lang", ScType.NODE_CONST_ROLE)

    private val intentAllSectionsSearch =
        context.resolveBySystemIdentifier("intent_all_sections_search", ScType.NODE_CONST)

    private val sectionLawBelarus = context.findBySystemIdentifier("section_law_belarus").get()
    private val nrelSectionDecomposition = context.findBySystemIdentifier("nrel_section_decomposition").get()

    override fun onTrigger(event: ScEvent) {

        val struct = event.payload[2]

        val intentAddr = context.getRoleRelationTarget(struct, rrelIntent)
        if (intentAddr != intentAllSectionsSearch) {
            return
        }

        val buf = context.getRelationSources(sectionLawBelarus, nrelSectionDecomposition, ScType.EDGE_D_COMMON_VAR)[0]

        val sections = context.api.searchByTemplate()
                .references(
                    ScReference.addr(buf),
                    ScReference.type(ScType.EDGE_ACCESS),
                    ScReference.type(ScType.NODE)
                )
                .execute().payload.addrs
        val answer = sections.map {
            context.getMainIdentifier(it[2], "lang_ru")
        }.joinToString("\n") {it}

        context.api.createElements()
            .link(ScType.LINK_CONST, answer, ScContentType.STRING)
            .edge(ScType.EDGE_ACCESS_CONST_POS_PERM, ScReference.addr(struct), ScReference.ref(0))
            .edge(ScType.EDGE_ACCESS_CONST_POS_PERM, ScReference.addr(rrelAnswerNaturalLang), ScReference.ref(1))
            .execute()
    }

}
