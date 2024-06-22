package com.study.studychatgpt.configuration;

import org.springframework.ai.openai.OpenAiChatClient;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAiChatConfig {

    @Value("${spring.ai.openai.chat.api-key}")
    private String apiKey;
    @Value("${spring.ai.openai.chat.base-url}")
    private String baseUrl;

    @Bean("myOpenAiChatClient")
    public OpenAiChatClient myOpenAiChatClient(){
        OpenAiApi openAiApi = new OpenAiApi(baseUrl, apiKey);
        return new OpenAiChatClient(openAiApi);
    }
}