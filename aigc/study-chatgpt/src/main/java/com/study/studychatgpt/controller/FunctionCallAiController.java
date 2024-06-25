package com.study.studychatgpt.controller;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.Generation;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.openai.OpenAiChatClient;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 函数调用模型
 * Function Calling 是大型语言模型连接外部的工具。
 */
@Slf4j
@RestController
public class FunctionCallAiController {

    @Resource(name = "myOpenAiChatClient")
    private OpenAiChatClient chatClient;


    @GetMapping("/ai/generate/function/call")
    public ResponseEntity functionCall(@RequestParam(value = "message", defaultValue = "上海天气如何?") String message) {

        String systemPrompt = "{prompt}";
        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(systemPrompt);
        Message systemMessage = systemPromptTemplate.createMessage(Map.of("prompt", "你是一个有用的人工智能助手"));

        Message userMessage = new UserMessage(message);
        Prompt prompt = new Prompt(List.of(userMessage, systemMessage),
                OpenAiChatOptions.builder().withFunctions(Set.of("currentWeather", "currentPopulation")).build());

        List<Generation> response = chatClient.call(prompt).getResults();
        String result = "";
        for (Generation generation : response) {
            String content = generation.getOutput().getContent();
            result += content;
        }
        return ResponseEntity.ok(result);
    }

}
