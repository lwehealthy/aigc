package com.study.studychatgpt.controller;

import com.alibaba.fastjson2.JSON;
import com.study.studychatgpt.dto.ActorsFilms;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.Generation;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.openai.OpenAiChatClient;
import org.springframework.ai.parser.BeanOutputParser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

/**
 * 对话模型
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

    /**
     * 提示词模板
     * 首先要创建包含动态内容占位符的模板，然后，这些占位符会根据用户请求或应用程序中的其他代码进行替换。
     * @param name
     * @param voice
     * @return
     */
    @GetMapping("/ai/prompt")
    public ResponseEntity prompt(@RequestParam(value = "name") String name,
                           @RequestParam(value = "voice") String voice) {
        String userText = """
                给我推荐上海的至少三个旅游景点
                """;

        Message userMessage = new UserMessage(userText);

        String systemText = """
                你是一个有用的人工智能助手，可以帮助人们查找信息，      
                你的名字是{name}，
                你应该用你的名字和{voice}的风格回复用户的请求。
                """;

        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(systemText);
        Message systemMessage = systemPromptTemplate.createMessage(Map.of("name", name, "voice", voice));

        Prompt prompt = new Prompt(List.of(userMessage, systemMessage));
        List<Generation> response = chatClient.call(prompt).getResults();
        StringBuilder result = new StringBuilder();
        for (Generation generation : response) {
            String content = generation.getOutput().getContent();
            result.append(content);
        }
        return ResponseEntity.ok(result);
    }

    /**
     * 结构化结果输出
     * @param actor
     * @return
     */
    @GetMapping("/ai/parser")
    public ResponseEntity Response(@RequestParam(value = "actor") String actor) {
        BeanOutputParser<ActorsFilms> outputParser = new BeanOutputParser<>(ActorsFilms.class);

        String userMessage = """
                为演员{actor}生成电影作品年表。
                {format}
                """;
        log.info("output format:{}", outputParser.getFormat());
        PromptTemplate promptTemplate = new PromptTemplate(userMessage, Map.of("actor", actor, "format", outputParser.getFormat()));
        Prompt prompt = promptTemplate.create();
        Generation generation = chatClient.call(prompt).getResult();

        ActorsFilms actorsFilms = outputParser.parse(generation.getOutput().getContent());
        return ResponseEntity.ok(JSON.toJSON(actorsFilms));
    }
}
