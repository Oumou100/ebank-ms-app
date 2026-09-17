package net.oumoudev.ebankbot.controllers;

import net.oumoudev.ebankbot.agents.EbankAiAgent;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
