package ostis.legislation.agent

import net.ostis.jesc.agent.ScAgent
import net.ostis.jesc.client.ScClient
import net.ostis.jesc.client.model.element.ScContentType
import net.ostis.jesc.client.model.element.ScReference
import net.ostis.jesc.client.model.response.ScEvent
import net.ostis.jesc.client.model.type.ScType
import ostis.legislation.StringSearch
import ostis.legislation.extension.*

class SectionDefinitionsAgent(event: Long, client: ScClient): ScAgent(setOf(event), client) {

    private val rrelIntent =
            context.resolveBySystemIdentifier("rrel_intent", ScType.NODE_CONST_ROLE)
    private val rrelFirstArgument =
            context.resolveBySystemIdentifier("rrel_first_argument", ScType.NODE_CONST_ROLE)
    private val rrelAnswerNaturalLang =
            context.resolveBySystemIdentifier("rrel_answer_natural_lang", ScType.NODE_CONST_ROLE)

    private val intentSectionDefinitionsSearch =
            context.resolveBySystemIdentifier("intent_section_definitions_search", ScType.NODE_CONST)

    private val rrelKeyScElement = context.findBySystemIdentifier("rrel_key_sc_element").get()

    private val sectionLawBelarus = context.findBySystemIdentifier("section_law_belarus").get()
    private val nrelSectionDecomposition = context.findBySystemIdentifier("nrel_section_decomposition").get()

    override fun onTrigger(event: ScEvent) {

        val struct = event.payload[2]

        val intentAddr = context.getRoleRelationTarget(struct, rrelIntent)
        if (intentAddr != intentSectionDefinitionsSearch) {
            return
        }

        val arg = context.getRoleRelationTarget(struct, rrelFirstArgument).let { context.getLinkContent(it) }.asText()

        val buf = context.getRelationSources(sectionLawBelarus, nrelSectionDecomposition, ScType.EDGE_D_COMMON_VAR)[0]

        val sections = context.api.searchByTemplate()
            .references(
                ScReference.addr(buf),
                ScReference.type(ScType.EDGE_ACCESS),
                ScReference.type(ScType.NODE)
            )
            .execute().payload.addrs

        var dictionaryAddrs = mutableListOf<Long>()
        for (section in sections) {
            var sectionAddr = section[2]

            val b = context.getRelationSource(sectionAddr, nrelSectionDecomposition, ScType.EDGE_D_COMMON_VAR)

            val dividers = context.api.searchByTemplate()
                .references(
                    ScReference.addr(b),
                    ScReference.type(ScType.EDGE_ACCESS),
                    ScReference.type(ScType.NODE)
                )
                .execute().payload.addrs

            for (divider in dividers){
                if (context.getSystemIdentifier(divider[2]).endsWith("_dict"))
                    dictionaryAddrs.add(divider[2])
            }

        }

        val idtfs = dictionaryAddrs.map { context.getMainIdentifier(it, "lang_ru") }
        val idtf = StringSearch.findMostSimilarString(arg, idtfs)

        val termAddr = dictionaryAddrs[idtfs.indexOf(idtf)]
        val answer = context.getRoleRelationTargets(termAddr, rrelKeyScElement)
            .map { context.getMainIdentifier(it, "lang_ru") }
            .joinToString (", " ){it}


        context.api.createElements()
            .link(ScType.LINK_CONST, answer, ScContentType.STRING)
            .edge(ScType.EDGE_ACCESS_CONST_POS_PERM, ScReference.addr(struct), ScReference.ref(0))
            .edge(ScType.EDGE_ACCESS_CONST_POS_PERM, ScReference.addr(rrelAnswerNaturalLang), ScReference.ref(1))
            .execute()

    }

}
