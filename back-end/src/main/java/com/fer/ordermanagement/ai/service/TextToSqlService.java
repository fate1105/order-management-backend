package com.fer.ordermanagement.ai.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fer.ordermanagement.ai.client.GeminiClient;
import com.fer.ordermanagement.ai.dto.QueryResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class TextToSqlService {

    private final GeminiClient geminiClient;
    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;

    private static final String SCHEMA = """
            Database schema:
            - orders(id, order_code, total_amount, status, created_at, updated_at, customer_id)
            - customers(id, full_name, phone, email, address, created_at, updated_at)
            - order_items(id, price, quantity, subtotal, order_id, product_id)
            - products(id, sku, name, price, description, status, created_at, updated_at, category_id)
            
            Rules:
            - Chỉ sinh ra câu SELECT, không INSERT/UPDATE/DELETE.
            - Trả về SQL thuần túy, KHÔNG bọc trong markdown (không dùng ```sql).
            - Không giải thích gì thêm.
            - Chỉ dùng tên bảng và cột đã liệt kê ở trên.
            """;

    public String toSql(String question) {
        String prompt = SCHEMA + "\nChuyển câu sau thành SQL:\n\"" + question + "\"";
        return geminiClient.ask(prompt);
    }

    public QueryResult query(String question) {
        String sql = toSql(question);
        validateSql(sql);
        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql);
        return new QueryResult(sql, results);
    }

    private void validateSql(String sql) {
        String upper = sql.toUpperCase().trim();
        if (!upper.startsWith("SELECT")) {
            throw new IllegalArgumentException("Chỉ cho phép câu SELECT");
        }
        List.of("DROP", "DELETE", "UPDATE", "INSERT", "ALTER", "TRUNCATE")
                .forEach(keyword -> {
                    if (upper.contains(keyword))
                        throw new SecurityException("Câu query không hợp lệ: " + keyword);
                });
    }

    public String chatWithData(String question) {
        // Bước 1: Lấy dữ liệu thô từ database
        QueryResult queryResult = query(question);

        // Nếu không có dữ liệu, trả lời luôn cho nhanh, đỡ tốn API
        if (queryResult.results().isEmpty()) {
            return "Dạ, em không tìm thấy dữ liệu nào phù hợp với yêu cầu của anh/chị ạ.";
        }

        try {
            // Bước 2: Chuyển kết quả list map thành chuỗi JSON
            String jsonData = objectMapper.writeValueAsString(queryResult.results());

            // Bước 3: Tạo prompt yêu cầu AI đóng vai nhân viên CSKH
            String prompt = String.format("""
                    Bạn là một trợ lý ảo thông minh cho một hệ thống quản lý đơn hàng.
                    Người dùng hỏi: "%s"
                    Dữ liệu thực tế lấy từ database (định dạng JSON): %s
                    
                    Yêu cầu:
                    - Dựa CHÍNH XÁC vào dữ liệu trên để trả lời câu hỏi.
                    - Trả lời bằng ngôn ngữ tự nhiên, thân thiện, ngắn gọn và dễ hiểu.
                    - KHÔNG nhắc đến việc bạn lấy dữ liệu từ JSON hay database.
                    - KHÔNG hiển thị code hay câu lệnh SQL.
                    """, question, jsonData);

            // Bước 4: Gọi Gemini để lấy câu trả lời cuối cùng
            return geminiClient.ask(prompt);

        } catch (JsonProcessingException e) {
            log.error("Lỗi parse JSON: ", e);
            return "Xin lỗi, hệ thống đang gặp lỗi khi xử lý dữ liệu.";
        }
    }
}