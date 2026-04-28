# Audit Domain

Provides comprehensive audit logging for all user actions across the system. This is an **internal-only** module with no public API endpoints.

## Responsibilities

- Record every significant user action (create, update, delete)
- Track which entity was affected and by whom
- Provide an audit trail for compliance and debugging

## Structure

```
audit/
├── entity/
│   └── AuditLog.java             # JPA entity for audit records
├── repository/
│   └── AuditLogRepository.java   # Data access layer
└── service/
    └── AuditLogService.java      # Logging service called by other domains
```

## Entity: `AuditLog`

| Column | Type | Description |
|--------|------|-------------|
| `id` | `Long` | Primary key (auto-generated) |
| `action` | `String(100)` | Action performed (e.g., `CREATE_ORDER`, `UPDATE_USER_STATUS`) |
| `entity` | `String(50)` | Entity type affected (e.g., `Order`, `User`) |
| `entity_id` | `Long` | ID of the affected entity (nullable) |
| `user_id` | `Long` | FK to the user who performed the action (nullable) |
| `created_at` | `LocalDateTime` | Timestamp (auto-set on creation) |

## Usage

Other services call `AuditLogService` to record actions:

```java
auditLogService.log(userId, "CREATE_ORDER", "Order", orderId);
```

## API Endpoints

None — this module is consumed internally by other domain services.

## Dependencies

- **Auth**: references `User` entity (ManyToOne relationship)
- **Consumed by**: Admin, Order, Product, Customer, Category, Payment services
