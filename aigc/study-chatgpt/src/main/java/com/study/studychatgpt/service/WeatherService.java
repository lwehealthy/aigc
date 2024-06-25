package com.study.studychatgpt.service;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import org.springframework.stereotype.Service;

import java.util.function.Function;

/**
 * 城市气温服务
 */
public class WeatherService implements Function<WeatherService.Request, WeatherService.Response> {

    /**
     * Weather Function request.
     * @JsonInclude(JsonInclude.Include.NON_NULL) 是 Jackson 库中用来指示在序列化 Java 对象时包含那些字段的注解。
     * 在这里，JsonInclude.Include.NON_NULL 表示在序列化过程中，只有字段的值不为 null 时才会被包含在输出中，null 值的字段将被忽略。
     *
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonClassDescription("Weather API request")
    public record Request(@JsonProperty(required = true, value = "位置")
                          @JsonPropertyDescription("城市，例如: 广州") String location) {
    }


    /**
     * Weather Function response.
     */
    public record Response(String weather) {
    }

    @Override
    public WeatherService.Response apply(WeatherService.Request request) {
        String weather = "";
        if (request.location().contains("上海")) {
            weather = "小雨转阴 13~19°C";
        } else if (request.location().contains("深圳")) {
            weather = "阴 15~26°C";
        } else {
            weather = "热到中暑 39-40°C";
        }
        return new WeatherService.Response(weather);
    }
}