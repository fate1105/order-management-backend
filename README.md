# 🛒 Order Management System — Backend

A scalable, production-ready backend service for managing orders, products, inventory, customers, and payments. Built with **Java 21** and **Spring Boot 3.5.9**, featuring JWT authentication, Role-Based Access Control, Redis caching, and AI-powered natural language querying via the Google Gemini API.

---

## 📋 Table of Contents

- [Architecture](#-architecture)
- [Tech Stack](#-tech-stack)
- [Features](#-features)
- [Database Schema](#-database-schema)
- [API Documentation](#-api-documentation)
- [Getting Started](#-getting-started)
- [Running Tests](#-running-tests)
- [CI/CD Pipeline](#-cicd-pipeline)
- [Project Structure](#-project-structure)
- [License](#-license)

---

## 🏗 Architecture

The application follows a **Layered Architecture** with domain-driven design principles:

```
┌─────────────────────────────────────────────────┐
│                  Client / Swagger UI             │
└──────────────────────┬──────────────────────────┘
                       │ HTTP / REST
┌──────────────────────▼──────────────────────────┐
│              Controller Layer                    │
│   AuthController · ProductController · ...       │
│   (Request validation, Swagger annotations)      │
├──────────────────────┬──────────────────────────┤
│              Service Layer                       │
│   AuthService · OrderServiceImpl · ...           │
│   (Business logic, transactions, caching)        │
├──────────────────────┬──────────────────────────┤
│            Repository Layer                      │
│   JPA Repositories + Custom JPQL Queries         │
│   (Data access, pessimistic locking)             │
├──────────────────────┬──────────────────────────┤
│              Domain Entities                     │
│   Order · Inventory · Product · ...              │
│   (Rich domain model with business rules)        │
└──────────────────────┬──────────────────────────┘
                       │
          ┌────────────┴────────────┐
          │                         │
    ┌─────▼─────┐           ┌──────▼──────┐
    │  MySQL 8   │           │    Redis     │
    │ (Primary)  │           │  (Cache)     │
    └────────────┘           └─────────────┘
```

**Key design decisions:**
- **Rich domain model** — Business logic lives inside entities (e.g., `Order.recalcTotal()`, `Inventory.reserve()`) instead of anemic models.
- **Pessimistic locking** on inventory operations to ensure data consistency under concurrent access.
- **Modular package structure** — Each business domain (`auth`, `order`, `product`, etc.) is a self-contained module with its own controller, service, repository, DTOs, and mappers.

---

## 🛠 Tech Stack

| Category | Technology |
|----------|-----------|
| **Language** | Java 21 |
| **Framework** | Spring Boot 3.5.9 |
| **Security** | Spring Security 6, JWT (jjwt 0.11.5) |
| **Database** | MySQL 8.x |
| **ORM** | Spring Data JPA / Hibernate |
| **Caching** | Redis (Spring Cache abstraction) |
| **API Docs** | OpenAPI 3 / Swagger UI (springdoc 2.8.6) |
| **AI** | Google Gemini API (Text-to-SQL, Chat) |
| **Testing** | JUnit 5, Mockito |
| **Build Tool** | Maven |
| **Utilities** | Lombok |
| **Container** | Docker (multi-stage build) |
| **CI/CD** | GitHub Actions |

---

## ✨ Features

### 🔐 Authentication & Authorization
- User registration and login with **JWT-based authentication**
- **Role-Based Access Control (RBAC)** with 3 roles: `ADMIN`, `STAFF`, `WAREHOUSE`
- Stateless session management
- BCrypt password encoding
- Locked account detection

### 📦 Product & Category Management
- Full CRUD operations for products and categories
- Product status lifecycle: `ACTIVE` → `OUT_OF_STOCK` → `DISCONTINUED`
- SKU-based product identification
- Pagination and keyword search

### 📋 Order Lifecycle Management
- Create orders with automatic **inventory reservation**
- Order status flow: `CREATED` → `CONFIRMED` → `PAID` → `COMPLETED` / `CANCELLED`
- Automatic **order total recalculation** when items are added
- Cancel orders with automatic **inventory release** and payment failure marking
- Search & filter orders by keyword and status with pagination

### 📊 Inventory Management
- Stock tracking with `quantity` and `reserved_quantity`
- **Pessimistic locking** (`SELECT ... FOR UPDATE`) for concurrent safety
- Operations: `reserve`, `release`, `fulfill`, `increase`
- Automatic inventory creation when products are added

### 💳 Payment Processing
- Payment methods: `COD`, `BANK_TRANSFER`, `MOCK`
- Payment status tracking: `PENDING` → `SUCCESS` / `FAILED`
- Linked to order lifecycle

### 👤 Customer Management
- Full CRUD for customers
- View customer order history
- Unique constraints on phone and email

### 📈 Admin Dashboard & Reports
- **Revenue report** — Revenue grouped by date within a date range
- **Order status report** — Count of orders by status
- **Top products report** — Best-selling products by quantity and revenue
- **Customer order history** — All orders for a specific customer
- **User management** — Update user roles and account status (lock/unlock)

### 🤖 AI-Powered Features (Google Gemini)
- **Text-to-SQL** — Convert natural language questions to SQL queries
- **Chat with Data** — Ask questions in Vietnamese, get natural language answers backed by real database data
- SQL injection prevention with whitelist validation

### 📝 Audit Logging
- Track user actions across the system (CREATE, UPDATE, DELETE, LOGIN, etc.)
- Records: user, action, entity type, entity ID, timestamp

### 🛡 Error Handling
- Global exception handler with consistent JSON response format
- Custom exceptions: `NotFoundException` (404), `ConflictException` (409), `BadRequestException` (400)
- Validation error formatting
- Security exception handling (401, 403)

### ⚡ Caching
- Redis-backed caching with 10-minute TTL
- Cached endpoints: order lookups, customer orders, admin reports
- JSON serialization with Java 8 Time support

---

## 🗄 Database Schema

The MySQL database consists of **9 tables** organized into logical groups:

```
┌─────────────┐     ┌─────────────┐
│    roles     │◄────│    users     │
└─────────────┘     └──────┬──────┘
                           │
                    ┌──────▼──────┐
                    │ audit_logs  │
                    └─────────────┘

┌─────────────┐     ┌─────────────┐     ┌─────────────┐
│ categories  │◄────│  products   │◄────│  inventory   │
└─────────────┘     └──────┬──────┘     └─────────────┘
                           │
                    ┌──────▼──────┐
┌─────────────┐     │ order_items │
│  customers  │◄────┤             │
└──────┬──────┘     └──────┬──────┘
       │                   │
       │            ┌──────▼──────┐     ┌─────────────┐
       └───────────►│   orders    │◄────│  payments    │
                    └─────────────┘     └─────────────┘
```

| Table | Description |
|-------|------------|
| `roles` | System roles (ADMIN, STAFF, WAREHOUSE) |
| `users` | User accounts with RBAC |
| `categories` | Product categories |
| `products` | Product catalog with SKU, pricing, status |
| `inventory` | Stock levels (quantity + reserved) |
| `customers` | Customer information |
| `orders` | Order headers with status lifecycle |
| `order_items` | Line items linking orders to products |
| `payments` | Payment records with method and status |
| `audit_logs` | System-wide audit trail |

---

## 📖 API Documentation

When the application is running, access the interactive **Swagger UI** at:

```
http://localhost:8080/swagger-ui.html
```

### API Endpoints Overview

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| **Auth** | | | |
| `POST` | `/auth/login` | 🔓 Public | User login |
| `POST` | `/auth/register` | 🔓 Public | User registration |
| **Products** | | | |
| `GET` | `/api/products` | 🔓 Public | List products (paginated) |
| `GET` | `/api/products/{id}` | 🔓 Public | Get product by ID |
| `POST` | `/api/products` | 🔒 Auth | Create product |
| `PUT` | `/api/products/{id}` | 🔒 Auth | Update product |
| `DELETE` | `/api/products/{id}` | 🔒 Auth | Delete product |
| **Categories** | | | |
| `GET` | `/api/categories` | 🔓 Public | List categories |
| `GET` | `/api/categories/{id}` | 🔓 Public | Get category by ID |
| `POST` | `/api/categories` | 🔒 Auth | Create category |
| `PUT` | `/api/categories/{id}` | 🔒 Auth | Update category |
| `DELETE` | `/api/categories/{id}` | 🔒 Auth | Delete category |
| **Orders** | | | |
| `POST` | `/api/orders` | 🔒 Auth | Create order |
| `GET` | `/api/orders/{id}` | 🔒 Auth | Get order by ID |
| `PUT` | `/api/orders/{id}/cancel` | 🔒 Auth | Cancel order |
| `GET` | `/api/orders` | 🔒 Auth | List orders (paginated, search) |
| `GET` | `/api/orders/customer/{id}` | 🔒 Auth | Orders by customer |
| **Customers** | | | |
| `POST` | `/api/customers` | 🔒 Auth | Create customer |
| `GET` | `/api/customers` | 🔒 Auth | List customers |
| `GET` | `/api/customers/{id}` | 🔒 Auth | Get customer by ID |
| `PUT` | `/api/customers/{id}` | 🔒 Auth | Update customer |
| `DELETE` | `/api/customers/{id}` | 🔒 Auth | Delete customer |
| **Payments** | | | |
| `PUT` | `/api/payments/{orderId}/confirm` | 🔒 Auth | Confirm payment |
| **AI** | | | |
| `POST` | `/api/ai/text-to-sql` | 🔒 Auth | Convert text to SQL |
| `POST` | `/api/ai/query` | 🔒 Auth | Query DB with natural language |
| `POST` | `/api/ai/chat` | 🔒 Auth | Chat with data |
| **Admin** | | | |
| `GET` | `/api/admin/users` | 🔒 Admin | List all users |
| `PUT` | `/api/admin/users/{id}/role` | 🔒 Admin | Update user role |
| `PUT` | `/api/admin/users/{id}/status` | 🔒 Admin | Lock/unlock user |
| `GET` | `/api/admin/reports/revenue` | 🔒 Admin | Revenue report |
| `GET` | `/api/admin/reports/order-status` | 🔒 Admin | Order status report |
| `GET` | `/api/admin/reports/top-products` | 🔒 Admin | Top selling products |
| `GET` | `/api/admin/reports/customer/{id}/history` | 🔒 Admin | Customer order history |

---

## 🚀 Getting Started

### Prerequisites

- **Java 21** (JDK)
- **Maven 3.9+** (or use the included Maven wrapper)
- **MySQL 8.x**
- **Redis** (for caching)
- **Docker & Docker Compose** (optional, for containerized setup)

### Option 1: Docker Compose (Recommended)

The fastest way to get everything running:

```bash
# 1. Clone the repository
git clone https://github.com/your-username/order-management-backend.git
cd order-management-backend

# 2. Configure environment variables
cp docker/.env.example docker/.env
# Edit docker/.env with your settings

# 3. Start all services (MySQL + App)
cd docker
docker-compose up -d

# 4. The app is running at http://localhost:8080
#    Swagger UI: http://localhost:8080/swagger-ui.html
```

### Option 2: Local Development

```bash
# 1. Clone the repository
git clone https://github.com/your-username/order-management-backend.git
cd order-management-backend

# 2. Set up MySQL database
mysql -u root -p < db/schema.sql
mysql -u root -p < db/seed.sql

# 3. Start Redis
redis-server

# 4. Configure environment variables
export JWT_SECRET=your-secret-key
export JWT_EXPIRATION=86400000
export GOOGLE_GEMINI_API_KEY=your-gemini-api-key

# 5. Build and run
cd back-end
./mvnw spring-boot:run

# 6. Access the application
#    API: http://localhost:8080
#    Swagger UI: http://localhost:8080/swagger-ui.html
```

### Environment Variables

| Variable | Description | Example |
|----------|------------|---------|
| `JWT_SECRET` | Secret key for JWT signing | `404E635266556A586E3272...` |
| `JWT_EXPIRATION` | Token expiration (ms) | `86400000` (24h) |
| `GOOGLE_GEMINI_API_KEY` | Google Gemini API key | `AIzaSy...` |
| `MYSQL_DATABASE` | Database name | `orders_db` |
| `MYSQL_USER` | MySQL username | `fer` |
| `MYSQL_PASSWORD` | MySQL password | `fer123` |

---

## 🧪 Running Tests

The project includes **62 unit tests** covering service layer logic with JUnit 5 and Mockito.

```bash
cd back-end

# Run all tests
./mvnw test

# Run tests with detailed output
./mvnw test -Dtest.verbose=true

# Run tests for a specific module
./mvnw test -Dtest=OrderServiceImplTest
./mvnw test -Dtest=ProductServiceImplTest
```

### Test Coverage

| Module | Test File | Tests |
|--------|-----------|-------|
| Auth | `AuthServiceTest` | Login, Register, Duplicate detection |
| Product | `ProductServiceImplTest` | CRUD, Search, Pagination, Status |
| Category | `CategoryServiceImplTest` | CRUD, Duplicate name, Active/Inactive |
| Order | `OrderServiceImplTest` | Create, Cancel, GetById, GetByCustomer, Paging |
| Customer | `CustomerServiceImplTest` | CRUD, Duplicate phone/email, Pagination |
| Inventory | `InventoryServiceImplTest` | Reserve, Release, Increase, Insufficient stock |

---

## 🔄 CI/CD Pipeline

The project uses **GitHub Actions** for continuous integration:

```
Push to main / PR → Run Unit Tests → Build JAR → Build Docker Image → Push to Docker Hub
```

### Pipeline Steps

1. **Checkout** — Clone the repository
2. **Setup JDK 21** — Configure Java with Temurin distribution and Maven caching
3. **Run Unit Tests** — Execute all 62 tests; pipeline fails if any test fails
4. **Build JAR** — Package the application with Maven
5. **Docker Login** — Authenticate with Docker Hub (main branch only)
6. **Build & Push Docker Image** — Multi-tag push: `latest`, `run_number`, `commit_sha`

---

## 📁 Project Structure

```
order-management-backend/
├── .github/
│   └── workflows/
│       └── ci.yml                    # GitHub Actions CI pipeline
├── back-end/
│   ├── src/
│   │   ├── main/java/com/fer/ordermanagement/
│   │   │   ├── admin/                # Admin user management & reports
│   │   │   │   ├── controller/       # AdminUserController, ReportController
│   │   │   │   ├── dto/              # Report & user DTOs
│   │   │   │   └── service/          # AdminUserService, ReportService
│   │   │   ├── ai/                   # AI-powered features
│   │   │   │   ├── client/           # GeminiClient (REST client)
│   │   │   │   ├── controller/       # TextToSqlController
│   │   │   │   ├── dto/              # QueryResult, TextToSqlRequest
│   │   │   │   └── service/          # TextToSqlService
│   │   │   ├── audit/                # Audit logging
│   │   │   │   ├── entity/           # AuditLog
│   │   │   │   ├── repository/       # AuditLogRepository
│   │   │   │   └── service/          # AuditLogService
│   │   │   ├── auth/                 # Authentication & authorization
│   │   │   │   ├── controller/       # AuthController (login/register)
│   │   │   │   ├── dto/              # LoginRequest, RegisterRequest, AuthResponse
│   │   │   │   ├── entity/           # User, Role
│   │   │   │   ├── enums/            # RoleName, UserStatus
│   │   │   │   ├── repository/       # UserRepository, RoleRepository
│   │   │   │   ├── security/         # SecurityConfig, JwtUtil, JwtAuthFilter
│   │   │   │   └── service/          # AuthService
│   │   │   ├── category/             # Category management
│   │   │   ├── common/               # Shared infrastructure
│   │   │   │   ├── config/           # RedisConfig, OpenApiConfig
│   │   │   │   ├── exception/        # GlobalExceptionHandler, custom exceptions
│   │   │   │   └── response/         # BaseResponse, PageResponse
│   │   │   ├── customer/             # Customer management
│   │   │   ├── inventory/            # Inventory management
│   │   │   ├── order/                # Order lifecycle management
│   │   │   ├── payment/              # Payment processing
│   │   │   └── product/              # Product catalog
│   │   ├── main/resources/
│   │   │   └── application.yaml      # Application configuration
│   │   └── test/                     # 62 unit tests (JUnit 5 + Mockito)
│   ├── Dockerfile                    # Multi-stage Docker build
│   └── pom.xml                       # Maven dependencies
├── db/
│   ├── schema.sql                    # Database schema (9 tables)
│   └── seed.sql                      # Sample data
├── docker/
│   ├── docker-compose.yml            # MySQL + App orchestration
│   ├── .env.example                  # Environment template
│   └── .env                          # Local environment (git-ignored)
├── LICENSE
└── README.md
```

---

## 📄 License

This project is licensed under the MIT License — see the [LICENSE](LICENSE) file for details.
