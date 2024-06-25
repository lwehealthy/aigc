package com.study.studychatgpt.controller;

import org.springframework.ai.image.ImageClient;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ImageAiController {

    @Autowired
    private ImageClient imageClient;

    /**
     * 图片生成
     *
     * @param description
     * @return
     */
    @GetMapping("/ai/image")
    public ResponseEntity image(@RequestParam(value = "description") String description) {
        ImageResponse response = imageClient.call(
                new ImagePrompt(description,
                        OpenAiImageOptions.builder().withQuality("hd").withN(1).withHeight(1024).withWidth(1024).build()));
        return ResponseEntity.ok(response.getResults().get(0).getOutput().getUrl());
    }
}
