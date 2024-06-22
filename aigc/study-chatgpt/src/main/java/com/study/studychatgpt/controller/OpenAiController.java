package com.study.studychatgpt.controller;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * chat模型
 * Chat模型会根据用户的输入，调用大模型，返回大模型的结果。
 */
@Slf4j
@RestController
public class OpenAiController {

    @Resource(name = "myOpenAiChatClient")
    private OpenAiChatClient chatClient;

    /**
     * 非流式输出 call：
     *      等待大模型把回答结果全部生成后输出给用户；
     * @param message
     * @return
     */
    @GetMapping("/ai/generate")
    public ResponseEntity generate(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        log.info("非流式输出——message:[{}]",message);
        return ResponseEntity.ok(chatClient.call(message));
    }

    /**
     * 流式输出 stream：
     *      逐个字符输出，一方面符合大模型生成方式的本质，
     *      另一方面当模型推理效率不是很高时，流式输出比起全部生成后再输出大大提高用户体验。
     * @param message
     * @return
     */
    @GetMapping("/ai/generateStream")
    public Flux<ChatResponse> generateStream(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        log.info("流式输出——message:[{}]",message);
        Prompt prompt = new Prompt(new UserMessage(message));
        return chatClient.stream(prompt);
    }
}
