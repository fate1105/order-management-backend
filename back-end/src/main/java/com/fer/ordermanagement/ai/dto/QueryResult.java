package com.fer.ordermanagement.ai.dto;

import java.util.List;
import java.util.Map;

public record QueryResult(
        String generatedSql,
        List<Map<String, Object>> results
) {}
