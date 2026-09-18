package net.oumoudev.ebankbot.agents;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class EbankAiAgent {
    private ChatClient chatClient;

    public EbankAiAgent(ChatClient.Builder chatClient, ChatMemory chatMemory, ToolCallbackProvider tools) {
        this.chatClient = chatClient
                .defaultSystem("""
                        Vous êtes un assistant qui se charge de répondre aux questions 
                        de l'utilisateur à propos des clients et des comptes bancaires, en fonction du contexte fourni.
                        Si aucun contexte n'est fourni, répond avec JE NE SAIS PAS
                        """)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .defaultToolCallbacks(tools)
                .build();
    }

    public String chat(String query, String conversationId){
        String safeConversationId = normalizeConversationId(conversationId);
        return chatClient.prompt()
                .user(query)
                .advisors(advisor -> advisor.param(ChatMemory.CONVERSATION_ID, safeConversationId))
                .call()
                .content();
    }

    public Flux<String> chatStream(Prompt prompt, String conversationId){
        String safeConversationId = normalizeConversationId(conversationId);
        return chatClient.prompt(prompt)
                .advisors(advisor -> advisor.param(ChatMemory.CONVERSATION_ID, safeConversationId))
                .stream()
                .content();
    }

    private String normalizeConversationId(String conversationId) {
        return conversationId == null || conversationId.isBlank()
                ? "default"
                : conversationId;
    }
}
