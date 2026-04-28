# Category Domain

Manages product categories. Each product belongs to a category, making this a foundational module for the product catalog.

## Responsibilities

- Create, update, retrieve, and delete product categories
- Manage category status (active/inactive)
- Provide category data for product association

## Structure

```
category/
├── controller/
│   ├── api/CategoryApi.java       # API interface with OpenAPI annotations
│   └── CategoryController.java    # REST controller implementation
├── dto/
│   ├── CategoryRequest.java       # Create/update request body
│   └── CategoryResponse.java      # Response DTO
├── entity/
│   └── Category.java              # JPA entity (name, status)
├── enums/
│   └── CategoryStatus.java        # Category status values
├── mapper/
│   └── CategoryMapper.java        # Entity ↔ DTO mapping
├── repository/
│   └── CategoryRepository.java
└── service/
    ├── CategoryService.java       # Service interface
    └── CategoryServiceImpl.java   # Business logic implementation
```

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/categories` | Create a new category |
| `PUT` | `/api/categories/{id}` | Update an existing category |
| `GET` | `/api/categories/{id}` | Get category by ID |
| `GET` | `/api/categories` | Get all categories |
| `DELETE` | `/api/categories/{id}` | Delete a category |

## Key Behaviors

- Category names should be unique
- Deleting a category that has associated products may be restricted
- All mutations are logged via `AuditLogService`

## Dependencies

- **Audit**: logs create/update/delete actions
- **Consumed by**: Product domain (each product references a category)
