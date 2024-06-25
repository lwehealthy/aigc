package com.study.studychatgpt.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * RAG 检索增强生成
 */
@Slf4j
@RestController
public class RagAIController {

    @Autowired
    private VectorStore vectorStore;

    @GetMapping("/ai/rag/create")
    public ResponseEntity ragCreate() {
        // 1. 提取文本内容
        String filePath = "/static/刘磊简历.txt";
        TextReader textReader = new TextReader(filePath);
        textReader.getCustomMetadata().put("filePath", filePath);
        List<Document> documents = textReader.get();
        log.info("documents before split:{}", documents);

        // 2. 文本切分为段落
        TextSplitter splitter = new TokenTextSplitter(1200, 350, 5, 100, true);
        documents = splitter.apply(documents);
        log.info("documents after split:{}", documents);

        // 3. 段落写入向量数据库
        vectorStore.add(documents);
        return ResponseEntity.ok().build();
    }
}
