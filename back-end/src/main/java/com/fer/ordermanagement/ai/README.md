# AI Domain

Integrates **Google Gemini API** to enable natural language database queries. Users can ask questions in plain English (or Vietnamese) and receive SQL translations, raw query results, or natural language answers.

## Responsibilities

- Convert natural language questions into SQL queries via Gemini
- Execute generated SQL against the database (SELECT only)
- Return raw data or AI-generated natural language answers

## Structure

```
ai/
├── client/
│   └── GeminiClient.java        # HTTP client for Google Gemini API
├── controller/
│   ├── api/TextToSqlApi.java     # API interface with OpenAPI annotations
│   └── TextToSqlController.java  # REST controller implementation
├── dto/
│   ├── TextToSqlRequest.java     # Input: natural language question
│   └── QueryResult.java          # Output: SQL + result rows
└── service/
    └── TextToSqlService.java     # Orchestrates text → SQL → execution → response
```

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/ai/to-sql` | Convert question to SQL (returns SQL string only) |
| `POST` | `/api/ai/query` | Convert question to SQL, execute it, return raw data |
| `POST` | `/api/ai/chat` | Ask a question, get a natural language answer based on data |

### Request Body

```json
{
  "question": "How many orders were placed last month?"
}
```

## Key Behaviors

- Only `SELECT` queries are permitted — any mutation attempts are rejected for data safety
- The database schema is provided to Gemini as context for accurate SQL generation
- `/chat` endpoint chains: question → SQL → execute → feed results back to Gemini → natural language answer

## Dependencies

- **External**: Google Gemini API (requires `GOOGLE_GEMINI_API_KEY` env variable)
- No direct dependency on other domain modules
