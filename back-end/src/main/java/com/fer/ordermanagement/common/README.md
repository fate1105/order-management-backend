# Common Module

Shared infrastructure components used across all domain modules. Contains response wrappers, global exception handling, and application-wide configuration.

## Responsibilities

- Provide a unified API response structure
- Handle exceptions globally with consistent error responses
- Configure OpenAPI/Swagger documentation
- Configure Redis caching

## Structure

```
common/
├── config/
│   ├── OpenApiConfig.java          # Swagger/OpenAPI configuration (title, auth scheme)
│   └── RedisConfig.java            # Redis cache manager configuration
├── exception/
│   ├── GlobalExceptionHandler.java # @RestControllerAdvice for centralized error handling
│   ├── BadRequestException.java    # 400 Bad Request
│   ├── NotFoundException.java      # 404 Not Found
│   └── ConflictException.java      # 409 Conflict
└── response/
    ├── BaseResponse.java           # Standard API response wrapper
    └── PageResponse.java           # Paginated response wrapper
```

## Response Structure

### `BaseResponse<T>`

All API responses are wrapped in this standard format:

```json
{
  "success": true,
  "status": 200,
  "message": "Success",
  "data": { ... },
  "errors": null,
  "timestamp": "2026-04-28T09:00:00"
}
```

### `PageResponse<T>`

Used for paginated list endpoints:

```json
{
  "content": [ ... ],
  "page": 0,
  "size": 10,
  "totalElements": 42,
  "totalPages": 5
}
```

## Exception Handling

`GlobalExceptionHandler` catches exceptions thrown anywhere in the application and maps them to consistent error responses:

| Exception | HTTP Status | Use Case |
|-----------|-------------|----------|
| `BadRequestException` | 400 | Invalid input, validation failures |
| `NotFoundException` | 404 | Entity not found by ID |
| `ConflictException` | 409 | Duplicate email, phone; delete with active references |
| `MethodArgumentNotValidException` | 400 | Bean validation (`@Valid`) failures |

## Dependencies

- **Consumed by**: All domain modules
