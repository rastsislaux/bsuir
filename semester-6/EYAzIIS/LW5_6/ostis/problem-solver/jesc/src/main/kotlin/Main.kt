import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.jackson.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.delay
import net.ostis.jesc.agent.ScAgentRegistry
import net.ostis.jesc.api.ScApi
import net.ostis.jesc.client.ScClient
import net.ostis.jesc.client.model.element.ScEventType
import net.ostis.jesc.client.model.element.ScReference
import net.ostis.jesc.client.model.type.ScType
import net.ostis.jesc.context.ScContext
import net.ostis.jesc.context.ScContextCommon
import ostis.legislation.WitAISCQueryCreator
import ostis.legislation.agent.ArticleContentAgent
import ostis.legislation.agent.FirstLetterSearchAgent
import ostis.legislation.agent.TelegramSendAnswerAgent
import ostis.legislation.agent.WhatIsAgent
import ostis.legislation.agent.*
import ostis.legislation.extension.getNoRoleRelationTarget
import ostis.legislation.thirdparty.telegram.Telegram
import ostis.legislation.thirdparty.witai.WitAI
import java.util.concurrent.atomic.AtomicLong

fun prepareContext(): ScContext {
    val scClient = ScClient("localhost", 8090)
    val scApi = ScApi(scClient)
    return ScContextCommon(scApi)
}

fun prepareTelegram(context: ScContext, witAI: WitAI) = Telegram(TELEGRAM_TOKEN).apply {
    newUpdateHandler {
        if (it.hasMessage()) {
            val queryCreator = WitAISCQueryCreator(context, witAI, it.message.chatId)
            queryCreator.createQuery(it.message.text)
        }
    }
}

data class Events(
    val naturalLangNewQuestion: Long,
    val naturalLangResultReady: Long,
    val firstLetterSearchNewQuestionEvent: Long,
    val articleContentNewQuestionEvent: Long,
    val allDefinitionsSearchNewQuestionEvent: Long,
    val allSectionsSearchNewQuestionEvent: Long,
    val allActsSearchEvent: Long,
    val findOriginQuestionEvent: Long,
    val findArticlesEvent: Long
)

fun prepareEvents(context: ScContext): Events {
    val questionNaturalLang = context.resolveBySystemIdentifier("question_natural_lang", ScType.NODE_CONST_CLASS)
    val rrelAnswerNaturalLang = context.resolveBySystemIdentifier("rrel_answer_natural_lang", ScType.NODE_CONST_ROLE)
    val questionJescFirstLetterSearch = context.resolveBySystemIdentifier("question_jesc_first_letter_search", ScType.NODE_CLASS)
    val questionJescArticleContent = context.resolveBySystemIdentifier("question_jesc_article_content", ScType.NODE_CONST_CLASS)
    val questionJescAllDefinitionsSearch = context.resolveBySystemIdentifier("question_jesc_section_definitions_search", ScType.NODE_CLASS)
    val questionJescAllSectionsSearch = context.resolveBySystemIdentifier("question_jesc_all_sections_search", ScType.NODE_CLASS)
    val questionJescAllActsSearch = context.resolveBySystemIdentifier("question_jesc_all_acts_search", ScType.NODE_CLASS)
    val questionJescFindOrigin = context.resolveBySystemIdentifier("question_jesc_find_origin", ScType.NODE_CLASS)
    val questionJescFindArticles = context.resolveBySystemIdentifier("question_jesc_find_articles", ScType.NODE_CLASS)

    return Events(
        naturalLangNewQuestion = context.createEvent(ScEventType.ADD_OUTGOING_EDGE, questionNaturalLang),
        naturalLangResultReady = context.createEvent(ScEventType.ADD_OUTGOING_EDGE, rrelAnswerNaturalLang),
        firstLetterSearchNewQuestionEvent = context.createEvent(ScEventType.ADD_OUTGOING_EDGE, questionJescFirstLetterSearch),
        articleContentNewQuestionEvent = context.createEvent(ScEventType.ADD_OUTGOING_EDGE, questionJescArticleContent),
        allDefinitionsSearchNewQuestionEvent = context.createEvent(ScEventType.ADD_OUTGOING_EDGE, questionJescAllDefinitionsSearch),
        allSectionsSearchNewQuestionEvent = context.createEvent(ScEventType.ADD_OUTGOING_EDGE, questionJescAllSectionsSearch),
        allActsSearchEvent = context.createEvent(ScEventType.ADD_OUTGOING_EDGE, questionJescAllActsSearch),
        findOriginQuestionEvent = context.createEvent(ScEventType.ADD_OUTGOING_EDGE, questionJescFindOrigin),
        findArticlesEvent = context.createEvent(ScEventType.ADD_OUTGOING_EDGE, questionJescFindArticles),
    )
}

fun main() {
    val witAI = WitAI(WIT_AI_TOKEN)
    val context = prepareContext()
    val agentRegistry = ScAgentRegistry(context.api.client)

    val telegram = prepareTelegram(context, witAI)
    val events = prepareEvents(context)

    val answers = mutableMapOf<Long, String>()

    agentRegistry.registerAgents(
        WhatIsAgent(events.naturalLangNewQuestion, context.api.client),
        TelegramSendAnswerAgent(telegram, events.naturalLangResultReady, context.api.client),
        HttpSendAnswerAgent(answers, events.naturalLangResultReady, context.api.client),
        FirstLetterSearchAgent(events.firstLetterSearchNewQuestionEvent, context.api.client),
        ArticleContentAgent(events.articleContentNewQuestionEvent, context.api.client),
        SectionDefinitionsAgent(events.allDefinitionsSearchNewQuestionEvent, context.api.client),
        AllSectionsSearchAgent(events.allSectionsSearchNewQuestionEvent,context.api.client),
        NumberOfActsOfSectionAgent(events.allActsSearchEvent, context.api.client),
        FindOriginAgent(events.findOriginQuestionEvent, context.api.client),
        FindArticlesAgent(events.findArticlesEvent, context.api.client)
    )

    embeddedServer(Netty, port = 8080, module = { module(context, witAI, answers) } )
        .start(wait = true)

    Runtime.getRuntime().addShutdownHook(Thread {
        println("Shutting down JESC agents.")
    })
}

fun Application.module(context: ScContext, witAI: WitAI, answers: Map<Long, String>) {
    install(ContentNegotiation) {
        jackson {  }
    }

    install(CORS) {
        method(HttpMethod.Options)
        header(HttpHeaders.XForwardedProto)
        anyHost()
    }

    val requestIdx = AtomicLong(0)

    routing {

        post {
            val requestBody = call.receive<String>()

            val idx = requestIdx.incrementAndGet()
            val queryCreator = WitAISCQueryCreator(context, witAI, idx)
            queryCreator.createQuery(requestBody)

            var counter = 0
            while (!answers.containsKey(idx) && counter != 100) { delay(100); counter++ }

            if (!answers.containsKey(idx)) {
                throw IllegalStateException()
            }

            call.respond(answers[idx]!!)
        }

        post("/terms") {
            val nrelLegalAct = context.findBySystemIdentifier("nrel_legal_act").get()

            val termAddrs = context.api.searchByTemplate()
                .references(
                    ScReference.addr(context.findBySystemIdentifier("belarus_legal_term").get()),
                    ScReference.type(ScType.EDGE_ACCESS_VAR_POS_PERM),
                    ScReference.type(ScType.VAR)
                )
                .execute().payload.addrs.map { it[2] }

            val answer = termAddrs.map {
                val idtf = context.getMainIdentifier(it, "lang_ru")

                val docIdtf = null

                return@map mapOf(
                    "idtf" to idtf,
                    "from" to docIdtf,
                    "def" to findDefinition(context, it)
                )
            }
            call.respond(answer)
        }

    }
}

private fun findDefinition(context: ScContext, termAddr: Long): String {
    val nrelScTextTranslation = context.findBySystemIdentifier("nrel_sc_text_translation").get()
    val definitionAddr = context.findBySystemIdentifier("definition").get()

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

    return context.getLinkContent(linkAddr).asText()
}


const val TELEGRAM_TOKEN = "6130969742:AAFRmCjEQF37J4SIQQnPMmG_k--YdlGNR-I"
const val WIT_AI_TOKEN = "OCBKFILCPZAW4KVB4PWIMZXKVAFDRBUN"
