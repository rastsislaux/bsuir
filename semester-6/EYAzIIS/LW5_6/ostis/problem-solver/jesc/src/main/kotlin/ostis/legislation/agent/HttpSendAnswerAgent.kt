package ostis.legislation.agent

import net.ostis.jesc.agent.ScAgent
import net.ostis.jesc.client.ScClient
import net.ostis.jesc.client.model.element.ScReference
import net.ostis.jesc.client.model.response.ScEvent
import net.ostis.jesc.client.model.type.ScType
import ostis.legislation.extension.getRoleRelationTarget

class HttpSendAnswerAgent(
    private val answers: MutableMap<Long, String>,
    event: Long,
    client: ScClient
): ScAgent(setOf(event), client) {

    private val rrelTelegramChatId =
        context.resolveBySystemIdentifier("rrel_telegram_chat_id", ScType.NODE_CONST_ROLE)

    override fun onTrigger(event: ScEvent) {
        Thread.sleep(1000)
        val answerRelation = event.payload[2]

        val addrs = try {
            context.api.searchByTemplate()
                .references(
                    ScReference.type(ScType.VAR),
                    ScReference.addr(answerRelation),
                    ScReference.type(ScType.VAR)
                )
                .execute().payload.addrs[0]
        } catch (e: IndexOutOfBoundsException) {
            // TODO: Probably, for some reason event for edge creation is emitted before edge is actually created.
            // TODO: If so, then it is a bug in SC-machine.
            println("TODO: Fix this bug, dunno why it happens yet.")
            Thread.sleep(6000)
            context.api.searchByTemplate()
                .references(
                    ScReference.type(ScType.VAR),
                    ScReference.addr(answerRelation),
                    ScReference.type(ScType.VAR)
                )
                .execute().payload.addrs[0]
        }

        val telegramId = context.getRoleRelationTarget(addrs[0], rrelTelegramChatId)
            .let { context.getLinkContent(it).asLong() }
        val answerText = context.getLinkContent(addrs[2]).asText()

        answers[telegramId] = answerText
    }

}