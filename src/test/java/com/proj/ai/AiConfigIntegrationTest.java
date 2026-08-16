package com.proj.ai;

import dev.langchain4j.model.mistralai.MistralAiChatModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
// Provides a mock value to fulfill the @Value("${MISTRAL_API_KEY}") field requirement during testing
@TestPropertySource(properties = "MISTRAL_API_KEY=mock-test-key-value-12345")
class AiConfigIntegrationTest {

    @Autowired
    private MistralAiChatModel chatLanguageModel;

    @Autowired
    private Assistant assistant;

    @Autowired
    private SystemTools systemTools;

    @Test
    void contextLoadsAndBeansAreCreated() {
        // Verifies Spring discovered the config and generated the LLM runtime engines
        assertNotNull(chatLanguageModel, "MistralAiChatModel bean should be initialized");
        assertNotNull(assistant, "LangChain4j Assistant proxy service bean should be initialized");
        assertNotNull(systemTools, "SystemTools component bean should be initialized");
    }
}
