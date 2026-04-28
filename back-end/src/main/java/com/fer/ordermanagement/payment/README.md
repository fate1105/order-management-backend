# Payment Domain

Handles payment processing for orders. Supports multiple payment methods and tracks payment status transitions.

## Responsibilities

- Create payment records when orders are placed (called internally by Order service)
- Mark payments as successful or failed via API
- Update order status based on payment outcome

## Structure

```
payment/
├── controller/
│   └── PaymentController.java      # REST controller (no separate API interface)
├── entity/
│   └── Payment.java                # JPA entity (order, method, status, amount)
├── enums/
│   ├── PaymentMethod.java          # COD, BANK_TRANSFER, MOCK
│   └── PaymentStatus.java          # PENDING, SUCCESS, FAILED
├── repository/
│   └── PaymentRepository.java
└── service/
    ├── PaymentService.java         # Service interface
    └── PaymentServiceImpl.java     # Business logic implementation
```

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/payments/{orderId}/success` | Mark payment as successful |
| `POST` | `/api/payments/{orderId}/fail` | Mark payment as failed |

## Payment Lifecycle

```
PENDING → SUCCESS
    ↓
  FAILED
```

## Payment Methods

| Method | Description |
|--------|-------------|
| `COD` | Cash on delivery |
| `BANK_TRANSFER` | Bank transfer |
| `MOCK` | Mock/test payment |

## Key Behaviors

- Payment records are created automatically when an order is placed (initial status: `PENDING`)
- `markSuccess()` updates payment to `SUCCESS` and transitions the order to `PAID`
- `markFailed()` updates payment to `FAILED`
- All actions are logged via `AuditLogService`

## Dependencies

- **Order**: reads order data, updates order status on payment completion
- **Audit**: logs payment actions
- **Consumed by**: Order service (creates payment on order creation)
