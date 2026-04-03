package com.fer.ordermanagement.ai.controller;

import com.fer.ordermanagement.ai.controller.api.TextToSqlApi;
import com.fer.ordermanagement.ai.dto.QueryResult;
import com.fer.ordermanagement.ai.dto.TextToSqlRequest;
import com.fer.ordermanagement.ai.service.TextToSqlService;
import com.fer.ordermanagement.common.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class TextToSqlController implements TextToSqlApi {

    private final TextToSqlService textToSqlService;

    @Override
    public ResponseEntity<BaseResponse<String>> toSql(@RequestBody TextToSqlRequest request) {
        return ResponseEntity.ok(BaseResponse.success(textToSqlService.toSql(request.question())));
    }

    @Override
    public ResponseEntity<BaseResponse<QueryResult>> query(@RequestBody TextToSqlRequest request) {
        return ResponseEntity.ok(BaseResponse.success(textToSqlService.query(request.question())));
    }

    @Override
    public ResponseEntity<BaseResponse<String>> chatWithData(@RequestBody TextToSqlRequest request) {
        return ResponseEntity.ok(BaseResponse.success(textToSqlService.chatWithData(request.question())));
    }
}