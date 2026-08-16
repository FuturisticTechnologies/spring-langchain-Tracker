package com.proj.ai;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dev.langchain4j.model.mistralai.MistralAiChatModel;
import dev.langchain4j.service.AiServices;

@Configuration
public class AiConfig {
	
	
	// 1. Inject the parsed placeholder property string value straight into this field variable
    @Value("${MISTRAL_API_KEY}")
    private String mistralApiKey;
	
    @Bean
    public MistralAiChatModel chatLanguageModel() {
        // 2. Feed the injected property token value safely into your builder client setup
        return MistralAiChatModel.builder()
                .apiKey(this.mistralApiKey)
                .modelName("mistral-small-latest")
                .temperature(0.0)
                .build();
    }

	@Bean
	public Assistant assistant(MistralAiChatModel chatLanguageModel, SystemTools1 systemTools) {
		return AiServices.builder(Assistant.class)
				.chatModel(chatLanguageModel)
		 .tools(systemTools)
		 .build();
		
	}
	
}
