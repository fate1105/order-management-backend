# Product Domain

Manages the product catalog with full CRUD, search, filtering, and pagination support. Creating a product automatically initializes its inventory record.

## Responsibilities

- Create, update, retrieve, and delete products
- Search products by keyword and filter by status
- Paginated listing for large catalogs
- Trigger inventory creation on new product

## Structure

```
product/
├── controller/
│   ├── api/ProductApi.java         # API interface with OpenAPI annotations
│   └── ProductController.java      # REST controller implementation
├── dto/
│   ├── ProductCreateRequest.java   # Fields for creating a product
│   ├── ProductUpdateRequest.java   # Fields for updating a product
│   └── ProductResponse.java        # Response DTO
├── entity/
│   └── Product.java                # JPA entity (name, price, description, status, category)
├── enums/
│   └── ProductStatus.java          # Product status values
├── mapper/
│   └── ProductMapper.java          # Entity ↔ DTO mapping
├── repository/
│   └── ProductRepository.java
└── service/
    ├── ProductService.java         # Service interface
    └── ProductServiceImpl.java     # Business logic implementation
```

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/products` | Create a new product (auto-creates inventory) |
| `PUT` | `/api/products/{id}` | Update product details |
| `GET` | `/api/products/{id}` | Get product by ID |
| `GET` | `/api/products` | List products (keyword search, status filter, paginated) |
| `DELETE` | `/api/products/{id}` | Delete a product |

## Key Behaviors

- Creating a product calls `InventoryService.createForProduct()` to initialize stock tracking
- Supports search by keyword (name/description) and filter by `ProductStatus`
- Uses `PageResponse<T>` for paginated results
- All mutations are logged via `AuditLogService`

## Dependencies

- **Category**: each product belongs to a category (FK reference)
- **Inventory**: auto-creates inventory record on product creation
- **Audit**: logs create/update/delete actions
- **Consumed by**: Order domain (order items reference products)
