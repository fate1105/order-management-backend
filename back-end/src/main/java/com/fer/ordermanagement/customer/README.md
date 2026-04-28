# Customer Domain

Manages customer information with full CRUD, search, and order history retrieval.

## Responsibilities

- Create, update, retrieve, and delete customers
- Enforce unique email and phone number constraints
- Search customers by keyword (name, email, phone)
- View a customer's order history

## Structure

```
customer/
├── controller/
│   ├── api/CustomerApi.java        # API interface with OpenAPI annotations
│   └── CustomerController.java     # REST controller implementation
├── dto/
│   ├── CustomerRequest.java        # Create/update request body
│   ├── CustomerResponse.java       # Response DTO
│   └── CustomerOrderResponse.java  # Lightweight order info for history
├── entity/
│   └── Customer.java               # JPA entity (name, email, phone, address)
├── mapper/
│   └── CustomerMapper.java         # Entity ↔ DTO mapping
├── repository/
│   └── CustomerRepository.java
└── service/
    ├── CustomerService.java        # Service interface
    └── CustomerServiceImpl.java    # Business logic implementation
```

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/customers` | Create a new customer |
| `PUT` | `/api/customers/{id}` | Update customer info |
| `GET` | `/api/customers/{id}` | Get customer by ID |
| `GET` | `/api/customers` | List customers (keyword search, paginated) |
| `DELETE` | `/api/customers/{id}` | Delete customer (blocked if has active orders) |
| `GET` | `/api/customers/{id}/orders` | Get order history for a customer |

## Key Behaviors

- Duplicate email or phone number returns `409 Conflict`
- Deleting a customer with existing orders returns `409 Conflict`
- Keyword search matches against name, email, and phone fields
- All mutations are logged via `AuditLogService`

## Dependencies

- **Order**: queries orders by customer ID for history endpoint
- **Audit**: logs create/update/delete actions
- **Consumed by**: Order domain (each order references a customer)
