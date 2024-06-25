package com.study.studychatgpt.service;


import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import org.springframework.stereotype.Service;

import java.util.function.Function;

/**
 * 城市人口服务
 */
public class PopulationService implements Function<PopulationService.Request, PopulationService.Response> {

    /**
     * Population Function request.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonClassDescription("Population API request")
    public record Request(@JsonProperty(required = true, value = "位置")
                          @JsonPropertyDescription("城市，例如: 上海") String location) {
    }


    /**
     * Population Function response.
     */
    public record Response(Integer population) {
    }

    @Override
    public PopulationService.Response apply(PopulationService.Request request) {
        Integer population = 0;
        if (request.location().contains("上海")) {
            population = 20000000;
        } else if (request.location().contains("深圳")) {
            population = 10000000;
        } else {
            population = 5000000;
        }
        return new PopulationService.Response(population);
    }
}
