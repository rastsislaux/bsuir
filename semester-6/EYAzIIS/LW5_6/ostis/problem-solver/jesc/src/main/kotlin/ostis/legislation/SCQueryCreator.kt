package ostis.legislation

import net.ostis.jesc.client.model.element.ScContentType
import net.ostis.jesc.client.model.element.ScReference
import net.ostis.jesc.client.model.type.ScType
import net.ostis.jesc.context.ScContext
import ostis.legislation.thirdparty.witai.WitAI
import ostis.legislation.thirdparty.witai.WitAIResponse

interface SCQueryCreator {
    fun createQuery(naturalQuery: String)
}

class WitAISCQueryCreator(
    private val ctx: ScContext,
    private val witAI: WitAI,
    private val telegramChatId: Long? = null
): SCQueryCreator {

    private val nrelMainIdtf = ctx.findBySystemIdentifier("nrel_main_idtf").get()
    private val langRu = ctx.findBySystemIdentifier("lang_ru").get()

    private val questionNaturalLangAddr =
        ctx.resolveBySystemIdentifier("question_natural_lang", ScType.NODE_CONST_CLASS)
    private val questionJescFirstLetterSearch =
        ctx.resolveBySystemIdentifier("question_jesc_first_letter_search", ScType.NODE_CONST_CLASS)
    private val questionJescArticleContent =
        ctx.resolveBySystemIdentifier("question_jesc_article_content", ScType.NODE_CONST_CLASS)
    private val questionJescSectionDefinitionsSearch =
        ctx.resolveBySystemIdentifier("question_jesc_section_definitions_search", ScType.NODE_CONST_CLASS)
    private val questionJescAllSectionsSearch =
        ctx.resolveBySystemIdentifier("question_jesc_all_sections_search", ScType.NODE_CONST_CLASS)
    private val questionJescAllActsSearch =
        ctx.resolveBySystemIdentifier("question_jesc_all_acts_search", ScType.NODE_CONST_CLASS)
    private val questionJescFindOrigin =
        ctx.resolveBySystemIdentifier("question_jesc_find_origin", ScType.NODE_CONST_CLASS)
    private val questionJescFindArticles =
        ctx.resolveBySystemIdentifier("question_jesc_find_articles", ScType.NODE_CONST_CLASS)

    private val rrelTelegramChatId =
        ctx.resolveBySystemIdentifier("rrel_telegram_chat_id", ScType.NODE_CONST_ROLE)
    private val rrelIntent =
        ctx.resolveBySystemIdentifier("rrel_intent", ScType.NODE_CONST_ROLE)
    private val rrelFirstArgument =
        ctx.resolveBySystemIdentifier("rrel_first_argument", ScType.NODE_CONST_ROLE)
    private val rrelSecondArgument =
        ctx.resolveBySystemIdentifier("rrel_second_argument", ScType.NODE_CONST_ROLE)

    private val intentWhatIs =
        ctx.resolveBySystemIdentifier("intent_what_is", ScType.NODE_CONST)
    private val intentFirstLetterSearch =
        ctx.resolveBySystemIdentifier("intent_first_letter_search", ScType.NODE_CONST)
    private val intentArticleContent =
        ctx.resolveBySystemIdentifier("intent_article_content", ScType.NODE_CONST)
    private val intentSectionDefinitionsSearch =
        ctx.resolveBySystemIdentifier("intent_section_definitions_search", ScType.NODE_CONST)
    private val intentAllSectionsSearch =
        ctx.resolveBySystemIdentifier("intent_all_sections_search", ScType.NODE_CONST)
    private val intentAllActsSearch =
        ctx.resolveBySystemIdentifier("intent_all_acts_search", ScType.NODE_CONST)
    private val intentFindOrigin =
        ctx.resolveBySystemIdentifier("intent_find_origin", ScType.NODE_CONST)
    private val intentFindArticles =
        ctx.resolveBySystemIdentifier("intent_find_articles", ScType.NODE_CONST)

    override fun createQuery(naturalQuery: String) {
        println("New query: $naturalQuery")
        val witAIResult = witAI.process(naturalQuery)
        when (witAIResult.intents[0].name) {
            "what_is" -> whatIsHandler(witAIResult, naturalQuery)
            "first_letter_search" -> firstLetterSearchHandler(witAIResult, naturalQuery)
            "article_content" -> articleContentHandler(witAIResult, naturalQuery)
            "section_definitions_search" -> sectionDefinitionsSearch(witAIResult, naturalQuery)
            "all_sections_search" -> allSectionsSearch(naturalQuery)
            "number_of_acts" -> allActsSearch(witAIResult, naturalQuery)
            "find_origin" -> findOriginHandler(witAIResult, naturalQuery)
            "find_articles" -> findArticlesHandler(witAIResult, naturalQuery)
            else -> println("Unsupported intent: ${witAIResult.intents[0].name}");
        }
    }

    private fun findArticlesHandler(witAIResult: WitAIResponse, naturalQuery: String) {
        ctx.api.createElements()
            .node(ScType.NODE_CONST) // 0

            .link(ScType.LINK_CONST, witAIResult.entities["codex:codex"]!![0].body, ScContentType.STRING) // 1
            .link(ScType.LINK_CONST, telegramChatId, ScContentType.INT) // 2

            .edge(ScType.EDGE_ACCESS_CONST_POS_PERM, ScReference.ref(0), ScReference.addr(intentFindArticles)) // 3
            .edge(ScType.EDGE_ACCESS_CONST_POS_PERM, ScReference.ref(0), ScReference.ref(1)) // 4
            .edge(ScType.EDGE_ACCESS_CONST_POS_PERM, ScReference.ref(0), ScReference.ref(2)) // 5

            .edge(ScType.EDGE_ACCESS_CONST_POS_PERM, ScReference.addr(rrelIntent), ScReference.ref(3)) // 6
            .edge(ScType.EDGE_ACCESS_CONST_POS_PERM, ScReference.addr(rrelFirstArgument), ScReference.ref(4)) // 7
            .edge(ScType.EDGE_ACCESS_CONST_POS_PERM, ScReference.addr(rrelTelegramChatId), ScReference.ref(5)) // 8

            .link(ScType.LINK_CONST, "Запрос на естественном языке: $naturalQuery", ScContentType.STRING) // 9
            .edge(ScType.EDGE_D_COMMON_CONST, ScReference.ref(0), ScReference.ref(9)) // 10
            .edge(ScType.EDGE_ACCESS_CONST_POS_PERM, ScReference.addr(nrelMainIdtf), ScReference.ref(10)) // 11
            .edge(ScType.EDGE_ACCESS_CONST_POS_PERM, ScReference.addr(langRu), ScReference.ref(11)) // 12

            .edge(ScType.EDGE_ACCESS_CONST_POS_PERM, ScReference.addr(questionNaturalLangAddr), ScReference.ref(0)) // 13
            .edge(ScType.EDGE_ACCESS_CONST_POS_PERM, ScReference.addr(questionJescFindArticles), ScReference.ref(0)) // 14

            .execute()
    }

    private fun articleContentHandler(witAIResult: WitAIResponse, naturalQuery: String) {
        ctx.api.createElements()
            .node(ScType.NODE_CONST) // 0

            .link(ScType.LINK_CONST, witAIResult.entities["codex:codex"]!![0].body, ScContentType.STRING) // 1
            .link(ScType.LINK_CONST, witAIResult.entities["wit\$number:number"]!![0].body, ScContentType.INT) // 2
            .link(ScType.LINK_CONST, telegramChatId, ScContentType.INT) // 3

            .edge(ScType.EDGE_ACCESS_CONST_POS_PERM, ScReference.ref(0), ScReference.addr(intentArticleContent)) // 4
            .edge(ScType.EDGE_ACCESS_CONST_POS_PERM, ScReference.ref(0), ScReference.ref(1)) // 5
            .edge(ScType.EDGE_ACCESS_CONST_POS_PERM, ScReference.ref(0), ScReference.ref(2)) // 6
            .edge(ScType.EDGE_ACCESS_CONST_POS_PERM, ScReference.ref(0), ScReference.ref(3)) // 7

            .edge(ScType.EDGE_ACCESS_CONST_POS_PERM, ScReference.addr(rrelIntent), ScReference.ref(4)) // 8
            .edge(ScType.EDGE_ACCESS_CONST_POS_PERM, ScReference.addr(rrelFirstArgument), ScReference.ref(5)) // 9
            .edge(ScType.EDGE_ACCESS_CONST_POS_PERM, ScReference.addr(rrelSecondArgument), ScReference.ref(6)) // 10
            .edge(ScType.EDGE_ACCESS_CONST_POS_PERM, ScReference.addr(rrelTelegramChatId), ScReference.ref(7)) // 11

            .link(ScType.LINK_CONST, "Запрос на естественном языке: $naturalQuery", ScContentType.STRING) // 12
            .edge(ScType.EDGE_D_COMMON_CONST, ScReference.ref(0), ScReference.ref(12)) // 13
            .edge(ScType.EDGE_ACCESS_CONST_POS_PERM, ScReference.addr(nrelMainIdtf), ScReference.ref(13)) // 14
            .edge(ScType.EDGE_ACCESS_CONST_POS_PERM, ScReference.addr(langRu), ScReference.ref(12)) // 15

            .edge(ScType.EDGE_ACCESS_CONST_POS_PERM, ScReference.addr(questionJescArticleContent), ScReference.ref(0)) // 16

            .execute()
    }

    private fun allActsSearch(witAIResult: WitAIResponse, naturalQuery: String) {
        ctx.api.createElements()
            .node(ScType.NODE_CONST) // 0

            .link(ScType.LINK_CONST, witAIResult.entities["codex:codex"]!![0].body, ScContentType.STRING) // 1
            .link(ScType.LINK_CONST, telegramChatId, ScContentType.INT) // 2

            .edge(ScType.EDGE_ACCESS_CONST_POS_PERM, ScReference.ref(0), ScReference.addr(intentAllActsSearch)) // 3
            .edge(ScType.EDGE_ACCESS_CONST_POS_PERM, ScReference.ref(0), ScReference.ref(1)) // 4
            .edge(ScType.EDGE_ACCESS_CONST_POS_PERM, ScReference.ref(0), ScReference.ref(2)) // 5

            .edge(ScType.EDGE_ACCESS_CONST_POS_PERM, ScReference.addr(rrelIntent), ScReference.ref(3))//6
            .edge(ScType.EDGE_ACCESS_CONST_POS_PERM, ScReference.addr(rrelFirstArgument), ScReference.ref(4))//7
            .edge(ScType.EDGE_ACCESS_CONST_POS_PERM, ScReference.addr(rrelTelegramChatId), ScReference.ref(5))//8
            .edge(ScType.EDGE_ACCESS_CONST_POS_PERM, ScReference.addr(questionJescAllActsSearch), ScReference.ref(0))//9

            .link(ScType.LINK_CONST, "Запрос на естественном языке: $naturalQuery", ScContentType.STRING) // 10
            .edge(ScType.EDGE_D_COMMON_CONST, ScReference.ref(0), ScReference.ref(10)) // 11
            .edge(ScType.EDGE_ACCESS_CONST_POS_PERM, ScReference.addr(nrelMainIdtf), ScReference.ref(11)) // 12
            .edge(ScType.EDGE_ACCESS_CONST_POS_PERM, ScReference.addr(langRu), ScReference.ref(10))

            .execute()
    }

    private fun allSectionsSearch(naturalQuery: String) {
        ctx.api.createElements()
            .node(ScType.NODE_CONST) // 0
            .link(ScType.LINK_CONST, telegramChatId, ScContentType.INT)// 1

            .edge(ScType.EDGE_ACCESS_CONST_POS_PERM, ScReference.ref(0), ScReference.addr(intentAllSectionsSearch)) // 2
            .edge(ScType.EDGE_ACCESS_CONST_POS_PERM, ScReference.ref(0), ScReference.ref(1)) // 3

            .edge(ScType.EDGE_ACCESS_CONST_POS_PERM, ScReference.addr(rrelIntent), ScReference.ref(2)) // 4
            .edge(ScType.EDGE_ACCESS_CONST_POS_PERM, ScReference.addr(rrelTelegramChatId), ScReference.ref(3)) // 5
            .edge(ScType.EDGE_ACCESS_CONST_POS_PERM, ScReference.addr(questionJescAllSectionsSearch), ScReference.ref(0)) // 6

            .link(ScType.LINK_CONST, "Запрос на естественном языке: $naturalQuery", ScContentType.STRING) // 7
            .edge(ScType.EDGE_D_COMMON_CONST, ScReference.ref(0), ScReference.ref(7)) // 8
            .edge(ScType.EDGE_ACCESS_CONST_POS_PERM, ScReference.addr(nrelMainIdtf), ScReference.ref(8)) // 9
            .edge(ScType.EDGE_ACCESS_CONST_POS_PERM, ScReference.addr(langRu), ScReference.ref(7)) // 10

            .execute()
    }

    private fun sectionDefinitionsSearch(witAIResult: WitAIResponse, naturalQuery: String) {
        ctx.api.createElements()
            .node(ScType.NODE_CONST) // 0
            .link(ScType.LINK_CONST, witAIResult.entities["section:section"]!![0].body, ScContentType.STRING) // 1
            .link(ScType.LINK_CONST, telegramChatId, ScContentType.INT) // 2

            .edge(ScType.EDGE_ACCESS_CONST_POS_PERM, ScReference.ref(0), ScReference.addr(intentSectionDefinitionsSearch)) // 3
            .edge(ScType.EDGE_ACCESS_CONST_POS_PERM, ScReference.ref(0), ScReference.ref(1)) // 4
            .edge(ScType.EDGE_ACCESS_CONST_POS_PERM, ScReference.ref(0), ScReference.ref(2)) // 5

            .edge(ScType.EDGE_ACCESS_CONST_POS_PERM, ScReference.addr(rrelIntent), ScReference.ref(3))//6
            .edge(ScType.EDGE_ACCESS_CONST_POS_PERM, ScReference.addr(rrelFirstArgument), ScReference.ref(4))//7
            .edge(ScType.EDGE_ACCESS_CONST_POS_PERM, ScReference.addr(rrelTelegramChatId), ScReference.ref(5))//8
            .edge(ScType.EDGE_ACCESS_CONST_POS_PERM, ScReference.addr(questionJescSectionDefinitionsSearch), ScReference.ref(0))//9

            .link(ScType.LINK_CONST, "Запрос на естественном языке: $naturalQuery", ScContentType.STRING) // 10
            .edge(ScType.EDGE_D_COMMON_CONST, ScReference.ref(0), ScReference.ref(10)) // 11
            .edge(ScType.EDGE_ACCESS_CONST_POS_PERM, ScReference.addr(nrelMainIdtf), ScReference.ref(11)) // 12
            .edge(ScType.EDGE_ACCESS_CONST_POS_PERM, ScReference.addr(langRu), ScReference.ref(10))

            .execute()
    }

    private fun findOriginHandler(witAIResult: WitAIResponse, naturalQuery: String) {
        ctx.api.createElements()
            .node(ScType.NODE_CONST) // 0

            .link(ScType.LINK_CONST, witAIResult.entities["term:term"]!![0].body, ScContentType.STRING) // 1
            .link(ScType.LINK_CONST, telegramChatId, ScContentType.INT) // 2

            .edge(ScType.EDGE_ACCESS_CONST_POS_PERM, ScReference.ref(0), ScReference.addr(intentFindOrigin)) // 3
            .edge(ScType.EDGE_ACCESS_CONST_POS_PERM, ScReference.ref(0), ScReference.ref(1)) // 4
            .edge(ScType.EDGE_ACCESS_CONST_POS_PERM, ScReference.ref(0), ScReference.ref(2)) // 5

            .edge(ScType.EDGE_ACCESS_CONST_POS_PERM, ScReference.addr(rrelIntent), ScReference.ref(3)) // 6
            .edge(ScType.EDGE_ACCESS_CONST_POS_PERM, ScReference.addr(rrelFirstArgument), ScReference.ref(4)) // 7
            .edge(ScType.EDGE_ACCESS_CONST_POS_PERM, ScReference.addr(rrelTelegramChatId), ScReference.ref(5)) // 8
            .edge(ScType.EDGE_ACCESS_CONST_POS_PERM, ScReference.addr(questionJescFindOrigin), ScReference.ref(0)) // 9

            .link(ScType.LINK_CONST, "Запрос на естественном языке: $naturalQuery", ScContentType.STRING) // 10
            .edge(ScType.EDGE_D_COMMON_CONST, ScReference.ref(0), ScReference.ref(10)) // 11
            .edge(ScType.EDGE_ACCESS_CONST_POS_PERM, ScReference.addr(nrelMainIdtf), ScReference.ref(11)) // 12
            .edge(ScType.EDGE_ACCESS_CONST_POS_PERM, ScReference.addr(langRu), ScReference.ref(10)) // 13

            .execute()
    }

    private fun firstLetterSearchHandler(witAIResult: WitAIResponse, naturalQuery: String) {
        ctx.api.createElements()
            .node(ScType.NODE_CONST) // 0

            .link(ScType.LINK_CONST, witAIResult.entities["letter:letter"]!![0].body, ScContentType.STRING) // 1
            .link(ScType.LINK_CONST, telegramChatId, ScContentType.INT) // 2

            .edge(ScType.EDGE_ACCESS_CONST_POS_PERM, ScReference.ref(0), ScReference.addr(intentFirstLetterSearch)) // 3
            .edge(ScType.EDGE_ACCESS_CONST_POS_PERM, ScReference.ref(0), ScReference.ref(1)) // 4
            .edge(ScType.EDGE_ACCESS_CONST_POS_PERM, ScReference.ref(0), ScReference.ref(2)) // 5

            .edge(ScType.EDGE_ACCESS_CONST_POS_PERM, ScReference.addr(rrelIntent), ScReference.ref(3))
            .edge(ScType.EDGE_ACCESS_CONST_POS_PERM, ScReference.addr(rrelFirstArgument), ScReference.ref(4))
            .edge(ScType.EDGE_ACCESS_CONST_POS_PERM, ScReference.addr(rrelTelegramChatId), ScReference.ref(5))
            .edge(ScType.EDGE_ACCESS_CONST_POS_PERM, ScReference.addr(questionJescFirstLetterSearch), ScReference.ref(0))

            .link(ScType.LINK_CONST, "Запрос на естественном языке: $naturalQuery", ScContentType.STRING) // 10
            .edge(ScType.EDGE_D_COMMON_CONST, ScReference.ref(0), ScReference.ref(10)) // 11
            .edge(ScType.EDGE_ACCESS_CONST_POS_PERM, ScReference.addr(nrelMainIdtf), ScReference.ref(11)) // 12
            .edge(ScType.EDGE_ACCESS_CONST_POS_PERM, ScReference.addr(langRu), ScReference.ref(10))

            .execute()
    }

    private fun whatIsHandler(witAIResult: WitAIResponse, naturalQuery: String) {
        ctx.api.createElements()
            .node(ScType.NODE_CONST_CLASS) // 0

            .link(ScType.LINK_CONST, witAIResult.entities["term:term"]!![0].value, ScContentType.STRING) // 1
            .link(ScType.LINK_CONST, telegramChatId, ScContentType.INT)                                  // 2

            .edge(ScType.EDGE_ACCESS_CONST_POS_PERM, ScReference.ref(0), ScReference.addr(intentWhatIs)) // 3
            .edge(ScType.EDGE_ACCESS_CONST_POS_PERM, ScReference.ref(0), ScReference.ref(1))       // 4
            .edge(ScType.EDGE_ACCESS_CONST_POS_PERM, ScReference.ref(0), ScReference.ref(2))       // 5

            .edge(ScType.EDGE_ACCESS_CONST_POS_PERM, ScReference.addr(rrelIntent), ScReference.ref(3))        // 6
            .edge(ScType.EDGE_ACCESS_CONST_POS_PERM, ScReference.addr(rrelFirstArgument), ScReference.ref(4)) // 7
            .edge(ScType.EDGE_ACCESS_CONST_POS_PERM, ScReference.addr(rrelTelegramChatId), ScReference.ref(5))// 8
            .edge(ScType.EDGE_ACCESS_CONST_POS_PERM, ScReference.addr(questionNaturalLangAddr), ScReference.ref(0)) // 9

            .link(ScType.LINK_CONST, "Запрос на естественном языке: $naturalQuery", ScContentType.STRING) // 10
            .edge(ScType.EDGE_D_COMMON_CONST, ScReference.ref(0), ScReference.ref(10)) // 11
            .edge(ScType.EDGE_ACCESS_CONST_POS_PERM, ScReference.addr(nrelMainIdtf), ScReference.ref(11)) // 12
            .edge(ScType.EDGE_ACCESS_CONST_POS_PERM, ScReference.addr(langRu), ScReference.ref(10))

            .execute()
    }

}
