package net.oumoudev.ebankbot.discord;

import com.zgamelogic.discord.annotations.DiscordController;
import com.zgamelogic.discord.annotations.DiscordMapping;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.oumoudev.ebankbot.agents.EbankAiAgent;

@DiscordController
public class DiscordBot {

    private final EbankAiAgent ebankAiAgent;

    public DiscordBot(EbankAiAgent ebankAiAgent) {
        this.ebankAiAgent = ebankAiAgent;
    }

    @DiscordMapping
    private void perform(MessageReceivedEvent event) {

        if (event.getAuthor().isBot()) {
            return;
        }

        String query = event.getMessage().getContentRaw();
        if (query == null || query.isBlank()) {
            return;
        }

        // Un channel Discord = une conversation
        String conversationId = event.getChannel().getId();

        String response = ebankAiAgent.chat(
            query,
            conversationId
        );

        sendResponse(event, response);
    }

    private void sendResponse(MessageReceivedEvent event, String response) {
        if (response == null || response.isBlank()) {
            event.getChannel().sendMessage("Je n'ai pas pu générer de réponse.").queue();
            return;
        }

        for (int start = 0; start < response.length(); start += 2000) {
            int end = Math.min(start + 2000, response.length());
            event.getChannel().sendMessage(response.substring(start, end)).queue();
        }
    }
}
