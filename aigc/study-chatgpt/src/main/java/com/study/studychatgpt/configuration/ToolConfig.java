package com.study.studychatgpt.configuration;


import com.study.studychatgpt.service.PopulationService;
import com.study.studychatgpt.service.WeatherService;
import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.ai.model.function.FunctionCallback;
import org.springframework.ai.model.function.FunctionCallbackWrapper;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.ai.vectorstore.VectorStore;

import java.util.function.Function;

/**
 * 将bean注入到spring容器（两种类型均可，一种为FunctionCallback、一种为Function）
 * 注意要给函数加上描述，让大模型理解函数的功能，以便能够更好的触发函数调用。
 */
@Configuration
public class ToolConfig {

    @Bean
    public FunctionCallback weatherFunctionInfo() {
        return FunctionCallbackWrapper.builder(new WeatherService())
                .withName("currentWeather")
                .withDescription("获取当地的气温")
                .build();
    }

    @Bean
    @Description("获取当地的人口")
    public Function<PopulationService.Request, PopulationService.Response> currentPopulation() {
        return new PopulationService();
    }

    /**
     * 生成向量库Bean
     *
     * @param embeddingClient
     * @return
     */
    @Bean
    public VectorStore createVectorStore(EmbeddingClient embeddingClient) {
        return new SimpleVectorStore(embeddingClient);
    }

}
