# Inventory Domain

Tracks stock quantities for each product. This is an **internal-only** module with no public API endpoints — it is invoked by the Product and Order services.

## Responsibilities

- Initialize inventory when a new product is created
- Increase stock (e.g., restocking)
- Reserve stock when an order is placed
- Release reserved stock when an order is cancelled

## Structure

```
inventory/
├── entity/
│   └── Inventory.java              # JPA entity (product reference, quantity)
├── repository/
│   └── InventoryRepository.java
└── service/
    ├── InventoryService.java        # Service interface
    └── InventoryServiceImpl.java    # Business logic implementation
```

## Service Interface

```java
public interface InventoryService {
    void createForProduct(Product product);   // Initialize stock = 0
    void increase(Long productId, int amount); // Add stock (restock)
    void reserve(Long productId, int amount);  // Deduct stock (order placed)
    void release(Long productId, int amount);  // Return stock (order cancelled)
}
```

## Key Behaviors

- `reserve()` throws an exception if insufficient stock is available
- `release()` is called when an order is cancelled to restore the reserved quantity
- Each `Product` has exactly one `Inventory` record (1:1 relationship)

## API Endpoints

None — this module is consumed internally by other domain services.

## Dependencies

- **Product**: receives `Product` entity on creation
- **Consumed by**: Product service (creation), Order service (reserve/release on order create/cancel)
