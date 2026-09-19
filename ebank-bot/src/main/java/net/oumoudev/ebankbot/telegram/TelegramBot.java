package net.oumoudev.ebankbot.telegram;

import jakarta.annotation.PostConstruct;
import net.oumoudev.ebankbot.agents.EbankAiAgent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.GetMe;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
public class TelegramBot extends TelegramLongPollingBot {
    private static final Logger log = LoggerFactory.getLogger(TelegramBot.class);

    @Value("${telegram.token}")
    private String telegramBotToken;

    @Value("${telegram.username:ebank_bot}")
    private String telegramBotUsername;

    private final EbankAiAgent aiAgent;

    public TelegramBot(EbankAiAgent aiAgent) {
        this.aiAgent = aiAgent;
    }

    @PostConstruct
    public void registerTelegramBot() {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(this);
            String actualUsername = execute(new GetMe()).getUserName();
            log.info("Telegram bot registered successfully as @{}", actualUsername);
        } catch (TelegramApiException exception) {
            throw new IllegalStateException("Unable to register Telegram bot. Check the token and polling conflicts.", exception);
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        log.debug("Telegram update received: {}", update.getUpdateId());
        if (!update.hasMessage() || !update.getMessage().hasText()) {
            log.debug("Ignoring Telegram update without a text message");
            return;
        }

        Long chatId = update.getMessage().getChatId();
        String query = update.getMessage().getText().trim();
        if (query.isBlank()) {
            log.debug("Ignoring blank Telegram message from chat {}", chatId);
            return;
        }

        try {
            log.info("Processing Telegram message from chat {}", chatId);
            String response = aiAgent.chat(query, chatId.toString());
            sendResponse(chatId, response);
        } catch (RuntimeException exception) {
            log.error("Unable to process Telegram message from chat {}", chatId, exception);
            try {
                sendResponse(chatId, "Une erreur est survenue pendant le traitement de votre demande.");
            } catch (RuntimeException sendException) {
                log.error("Unable to send Telegram error response to chat {}", chatId, sendException);
            }
        }
    }

    private void sendResponse(Long chatId, String response) {
        String safeResponse = response == null || response.isBlank()
                ? "Je n'ai pas pu générer de réponse."
                : response;

        for (int start = 0; start < safeResponse.length(); start += 4096) {
            int end = Math.min(start + 4096, safeResponse.length());
            SendMessage message = new SendMessage(chatId.toString(), safeResponse.substring(start, end));
            try {
                execute(message);
            } catch (TelegramApiException exception) {
                throw new IllegalStateException("Unable to send Telegram message to chat " + chatId, exception);
            }
        }
    }

    @Override
    public String getBotUsername() {
        return telegramBotUsername;
    }

    @Override
    public String getBotToken() {
        return telegramBotToken;
    }
}
