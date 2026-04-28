# Admin Domain

Provides administrative capabilities for user management and business reporting. All endpoints require `ADMIN` role.

## Responsibilities

- List, view, update, and delete user accounts
- Update user status (activate/lock) and roles (STAFF ↔ ADMIN)
- Generate revenue reports, order status statistics, top-selling products, and customer order history

## Structure

```
admin/
├── controller/
│   ├── api/
│   │   ├── AdminUserApi.java     # User management API interface
│   │   └── ReportApi.java        # Reporting API interface
│   ├── AdminUserController.java
│   └── ReportController.java
├── dto/
│   ├── user/
│   │   ├── UserResponse.java
│   │   ├── UpdateUserStatusRequest.java
│   │   └── UpdateUserRoleRequest.java
│   └── report/
│       ├── RevenueReportResponse.java
│       ├── OrderStatusReportResponse.java
│       ├── TopProductReportResponse.java
│       └── CustomerOrderHistoryResponse.java
└── service/
    ├── AdminUserService.java     # User CRUD + status/role updates
    └── ReportService.java        # Aggregated reporting queries
```

## API Endpoints

### User Management (`/api/admin/users`)

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/admin/users` | List users (filter by status, role; paginated) |
| `GET` | `/api/admin/users/{id}` | Get user details |
| `PUT` | `/api/admin/users/{id}/status` | Activate or lock a user account |
| `PUT` | `/api/admin/users/{id}/role` | Change user role |
| `DELETE` | `/api/admin/users/{id}` | Delete a user |

### Reports & Analytics (`/api/admin/reports`)

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/admin/reports/revenue` | Revenue report by date range (`startDate`, `endDate`) |
| `GET` | `/api/admin/reports/orders/status` | Order count grouped by status |
| `GET` | `/api/admin/reports/products/top` | Top-selling products (default: top 10) |
| `GET` | `/api/admin/reports/customers/{customerId}/orders` | Order history for a specific customer |

## Key Behaviors

- All actions are logged via `AuditLogService`
- Status/role updates include the authenticated admin's identity in audit logs
- Report queries use native/JPQL queries for aggregation

## Dependencies

- **Auth**: reads `User` and `Role` entities
- **Audit**: logs all admin actions
- **Order, Product, Customer**: queried for report generation
