package net.oumoudev.ebankbot.controllers;

import net.oumoudev.ebankbot.agents.EbankAiAgent;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class EbankChatbotController {
    private EbankAiAgent ebankAiAgent;

    public EbankChatbotController(EbankAiAgent ebankAiAgent) {

        this.ebankAiAgent = ebankAiAgent;
    }

    @GetMapping(value = "/chat", produces = MediaType.TEXT_PLAIN_VALUE)
    public String chat(
            @RequestParam(name = "query", defaultValue = "Bonjour") String query,
            @RequestParam(name = "conversationId", defaultValue = "default") String conversationId) {
        return ebankAiAgent.chat(query, conversationId);
    }


    @GetMapping(value = "/chattelegram", produces = MediaType.TEXT_PLAIN_VALUE)
    public String chatTelegram(
            @RequestParam(name = "query", defaultValue = "Bonjour") String query,
            @RequestParam(name = "conversationId", defaultValue = "default") String conversationId) {
        return ebankAiAgent.chatTelegram(query, conversationId);
    }
    @GetMapping(value = "/chatStream", produces = MediaType.TEXT_PLAIN_VALUE)
    public Flux<String> chatStream(
            @RequestParam(name = "query", defaultValue = "Bonjour") String query,
            @RequestParam(name = "conversationId", defaultValue = "default") String conversationId) {
        return ebankAiAgent.chatStream(new Prompt(query), conversationId);
    }
}
