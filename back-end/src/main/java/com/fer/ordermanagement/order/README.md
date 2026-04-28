# Order Domain

Core business domain handling order creation, listing, and cancellation. Orchestrates interactions with Customer, Product, Inventory, and Payment modules.

## Responsibilities

- Create orders with multiple line items
- Calculate order totals from product prices × quantities
- Reserve inventory on order creation
- Trigger payment record creation
- Cancel orders and release reserved inventory
- Search and filter orders by keyword and status

## Structure

```
order/
├── controller/
│   ├── api/OrderApi.java           # API interface with OpenAPI annotations
│   └── OrderController.java        # REST controller implementation
├── dto/
│   ├── OrderRequest.java           # Create order request (customerId, items, paymentMethod)
│   ├── OrderItemRequest.java       # Single line item (productId, quantity)
│   ├── OrderResponse.java          # Full order response DTO
│   └── OrderItemResponse.java      # Line item response DTO
├── entity/
│   ├── Order.java                  # JPA entity (customer, totalAmount, status, timestamps)
│   └── OrderItem.java              # JPA entity (product, quantity, unitPrice)
├── enums/
│   └── OrderStatus.java            # CREATED → CONFIRMED → PAID → COMPLETED | CANCELLED
├── mapper/
│   └── OrderMapper.java            # Entity ↔ DTO mapping
├── repository/
│   ├── OrderRepository.java
│   └── OrderItemRepository.java
└── service/
    ├── OrderService.java           # Service interface
    └── OrderServiceImpl.java       # Business logic implementation
```

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/orders` | Create a new order |
| `GET` | `/api/orders/{id}` | Get order details by ID |
| `GET` | `/api/orders` | List orders (keyword, status filter, paginated) |
| `PATCH` | `/api/orders/{id}/cancel` | Cancel an order |

## Order Lifecycle

```
CREATED → CONFIRMED → PAID → COMPLETED
   ↓         ↓
 CANCELLED  CANCELLED
```

## Key Behaviors

- **Create order flow**:
  1. Validate customer exists
  2. Validate all products exist and are active
  3. Reserve inventory for each line item (`InventoryService.reserve()`)
  4. Calculate total amount (Σ unitPrice × quantity)
  5. Save order + order items
  6. Create payment record (`PaymentService`)
- **Cancel order flow**:
  1. Verify order is in a cancellable state
  2. Release reserved inventory (`InventoryService.release()`)
  3. Update order status to `CANCELLED`
- All actions are logged via `AuditLogService`

## Dependencies

- **Customer**: validates customer existence
- **Product**: validates products and reads prices
- **Inventory**: reserves stock on create, releases on cancel
- **Payment**: creates payment record on order creation
- **Audit**: logs all order actions
- **Consumed by**: Admin (reporting), Customer (order history)
