package com.fer.ordermanagement.ai.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.List;
import java.util.Map;

@Component
public class GeminiClient {

    private final RestClient restClient;
    private final String apiUrl;

    // Inject cả API Key và Model Name từ application.yml
    public GeminiClient(RestClient.Builder restClientBuilder,
                        @Value("${google.gemini.api.key}") String apiKey,
                        @Value("${google.gemini.api.model:gemini-2.0-flash}") String model) { // Giá trị mặc định là gemini-2.0-flash

        // Gắn linh hoạt tên model vào URL
        this.apiUrl = "https://generativelanguage.googleapis.com/v1beta/models/" + model + ":generateContent";

        this.restClient = restClientBuilder
                .defaultHeader("x-goog-api-key", apiKey)
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public String ask(String prompt) {
        Map<String, Object> body = Map.of(
                "contents", List.of(
                        Map.of("parts", List.of(
                                Map.of("text", prompt)
                        ))
                )
        );

        try {
            Map response = restClient.post()
                    .uri(this.apiUrl) // Sử dụng URL đã build ở constructor
                    .body(body)
                    .retrieve()
                    .body(Map.class);

            if (response == null || !response.containsKey("candidates")) {
                return "Không nhận được phản hồi hợp lệ từ AI.";
            }

            var candidates = (List<Map>) response.get("candidates");
            if (candidates.isEmpty()) return "AI không có câu trả lời.";

            var content = (Map) candidates.get(0).get("content");
            var parts = (List<Map>) content.get("parts");

            return parts.get(0).get("text").toString().trim();

        } catch (RestClientException e) {
            System.err.println("Lỗi khi gọi Gemini API: " + e.getMessage());
            return "Xin lỗi, hệ thống AI đang gặp sự cố. Vui lòng thử lại sau.";
        } catch (Exception e) {
            System.err.println("Lỗi parse dữ liệu: " + e.getMessage());
            return "Lỗi xử lý câu trả lời từ AI.";
        }
    }
}