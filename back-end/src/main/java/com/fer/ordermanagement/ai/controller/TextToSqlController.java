package com.fer.ordermanagement.ai.controller;

import com.fer.ordermanagement.ai.service.TextToSqlService;
import com.fer.ordermanagement.ai.dto.QueryResult;
import com.fer.ordermanagement.ai.dto.TextToSqlRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class TextToSqlController {

    private final TextToSqlService textToSqlService;

    @PostMapping("/to-sql")
    public ResponseEntity<String> toSql(@RequestBody TextToSqlRequest request) {
        return ResponseEntity.ok(textToSqlService.toSql(request.question()));
    }

    @PostMapping("/query")
    public ResponseEntity<QueryResult> query(@RequestBody TextToSqlRequest request) {
        return ResponseEntity.ok(textToSqlService.query(request.question()));
    }

    @PostMapping("/chat")
    public ResponseEntity<String> chatWithData(@RequestBody TextToSqlRequest request) {
        // Kết quả trả về bây giờ là một câu nói hoàn chỉnh thay vì JSON
        return ResponseEntity.ok(textToSqlService.chatWithData(request.question()));
    }
}