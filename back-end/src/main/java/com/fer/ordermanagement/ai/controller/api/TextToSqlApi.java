package com.fer.ordermanagement.ai.controller.api;

import com.fer.ordermanagement.ai.dto.QueryResult;
import com.fer.ordermanagement.ai.dto.TextToSqlRequest;
import com.fer.ordermanagement.common.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "AI - Text to SQL", description = "Truy vấn dữ liệu bằng ngôn ngữ tự nhiên")
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping("/api/ai")
public interface TextToSqlApi {

    @Operation(summary = "Chuyển câu hỏi thành câu SQL")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Thành công"),
            @ApiResponse(responseCode = "400", description = "Câu hỏi không hợp lệ")
    })
    @PostMapping("/to-sql")
    ResponseEntity<BaseResponse<String>> toSql(@RequestBody TextToSqlRequest request);

    @Operation(summary = "Thực thi câu hỏi và trả về dữ liệu thô")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Thành công"),
            @ApiResponse(responseCode = "400", description = "Câu hỏi không hợp lệ")
    })
    @PostMapping("/query")
    ResponseEntity<BaseResponse<QueryResult>> query(@RequestBody TextToSqlRequest request);

    @Operation(summary = "Hỏi đáp với dữ liệu, trả về câu trả lời tự nhiên")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Thành công"),
            @ApiResponse(responseCode = "400", description = "Câu hỏi không hợp lệ")
    })
    @PostMapping("/chat")
    ResponseEntity<BaseResponse<String>> chatWithData(@RequestBody TextToSqlRequest request);
}