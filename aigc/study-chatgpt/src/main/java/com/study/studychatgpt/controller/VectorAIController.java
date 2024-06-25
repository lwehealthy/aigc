package com.study.studychatgpt.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.fastjson2.JSON;

import java.util.ArrayList;
import java.util.List;

/**
 * Embedding Client 旨在将大模型中的向量化功能直接集成。
 * 它的主要功能是将文本转换为数字矢量，通常称为向量化。
 */
@Slf4j
@RestController
public class VectorAIController {

    @Autowired
    private EmbeddingClient embeddingClient;

    @Autowired
    private VectorStore vectorStore;

    /**
     * 向量化
     * @param message
     * @return
     */
    @GetMapping("/ai/embedding")
    public ResponseEntity embed(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        EmbeddingResponse embeddingResponse = this.embeddingClient.embedForResponse(List.of(message));
        return ResponseEntity.ok(embeddingResponse);
    }

    /**
     * 写入向量库（包括向量化与写入向量库两步）
     * @param content
     * @return
     */
    @GetMapping("/ai/vectorStore/add")
    public ResponseEntity vectorStoreAdd(@RequestParam(value = "content") String content) {
        List<Document> documentList = new ArrayList<>();
        Document document = new Document(content);
        documentList.add(document);
        vectorStore.add(documentList);
        return ResponseEntity.ok().build();
    }

    /**
     * 检索向量库
     * @param query
     * @return
     */
    @GetMapping("/ai/vectorStore/search")
    public ResponseEntity vectorStoreSearch(@RequestParam(value = "query") String query) {
        List<Document> documents = vectorStore.similaritySearch(query);
        return ResponseEntity.ok(JSON.toJSON(documents));
    }
}
