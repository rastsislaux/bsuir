package ostis.legislation.thirdparty.telegram

import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession

class Telegram(token: String): TelegramLongPollingBot(token) {

    private val updateSubscribers: MutableList<(Update) -> Unit> = mutableListOf()

    init {
        TelegramBotsApi(DefaultBotSession::class.java).registerBot(this)
    }

    override fun getBotUsername() = "ostis_legislation_bot"

    override fun onUpdateReceived(update: Update) {
        updateSubscribers.forEach { it(update) }
    }

    fun newUpdateHandler(subscriber: (Update) -> Unit) {
        updateSubscribers.add(subscriber)
    }

    fun sendMessage(chatId: Long, text: String) {
        val sendMessage = SendMessage()
        sendMessage.chatId = chatId.toString()
        sendMessage.text = text

        sendApiMethod(sendMessage)
    }

}
