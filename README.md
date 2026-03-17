# Addi Products API

**Product Management API** — Enterprise solution for product catalog and administration with PostgreSQL persistence, Redis caching, and integrated web interface.

---

## Executive Summary

Addi Products API is a backend application built with **Kotlin** and **Spring Boot 4** that exposes REST services for product CRUD operations. It implements **Hexagonal Architecture**, **Factory pattern**, and **Dependency Injection**. Features include an integrated web interface, product search with Redis cache, and input validation.

| Aspect | Detail |
|--------|--------|
| **Version** | 0.0.1-SNAPSHOT |
| **Organization** | ia.dev.codytion |
| **Java** | 21 |
| **Framework** | Spring Boot 4.0.3 |

---

## Architecture

**Hexagonal Architecture (Ports & Adapters)**

```
                    ┌─────────────────────────────────────────────────────────┐
                    │                    INBOUND ADAPTERS                       │
                    │  ┌─────────────────────────────────────────────────┐    │
                    │  │  ProductRestController (REST API)                │    │
                    │  └──────────────────────────┬──────────────────────┘    │
                    └─────────────────────────────┼────────────────────────────┘
                                                  │
                    ┌─────────────────────────────┼────────────────────────────┐
                    │              APPLICATION (Use Cases)                      │
                    │  ┌──────────────────────────▼──────────────────────┐     │
                    │  │  ProductService ← ProductFactory (DI)            │     │
                    │  │  (+ Redis cache)                                 │     │
                    │  └──────────────────────────┬──────────────────────┘     │
                    └─────────────────────────────┼─────────────────────────────┘
                                                  │
                    ┌─────────────────────────────┼────────────────────────────┐
                    │                    DOMAIN                                 │
                    │  ┌──────────────┐  ┌────────▼────────┐                    │
                    │  │   Product    │  │ ProductRepositoryPort │               │
                    │  │  (entity)   │  │    (outbound port)    │               │
                    │  └──────────────┘  └────────┬────────┘                    │
                    └────────────────────────────┼─────────────────────────────┘
                                                  │
                    ┌─────────────────────────────┼────────────────────────────┐
                    │                   OUTBOUND ADAPTERS                       │
                    │  ┌──────────────────────────▼──────────────────────┐     │
                    │  │  ProductRepositoryAdapter → JPA → PostgreSQL     │     │
                    │  └─────────────────────────────────────────────────┘     │
                    └──────────────────────────────────────────────────────────┘
```

---

## Design Patterns

| Pattern | Implementation |
|---------|-----------------|
| **Hexagonal Architecture** | Domain, Application, Adapters (inbound/outbound) |
| **Factory** | `ProductFactory` — creates and validates Product entities |
| **Dependency Injection** | Constructor injection via Spring; dependencies on ports (interfaces) |
| **Ports & Adapters** | `ProductServicePort` (inbound), `ProductRepositoryPort` (outbound) |

---

## Technology Stack

| Technology | Purpose |
|------------|---------|
| **Kotlin 2.2** | Primary language |
| **Spring Boot 4** | Web and backend framework |
| **Spring Data JPA** | Persistence and data access |
| **PostgreSQL** | Relational database |
| **Spring Cache** | Query caching (memory or Redis) |
| **Spring Data Redis** | Distributed cache for product list and search |
| **Spring Actuator** | Monitoring and metrics |

---

## Project Structure (Hexagonal)

```
addi-products-api/
├── src/main/kotlin/ia/dev/codytion/addiproductsapi/
│   ├── AddiProductsApiApplication.kt
│   ├── CacheEvictOnStartup.kt         # Clears Redis cache on startup
│   ├── WebConfig.kt
│   ├── domain/                          # Core business logic
│   │   └── product/
│   │       ├── Product.kt               # Domain entity
│   │       └── port/
│   │           └── ProductRepositoryPort.kt
│   ├── application/                     # Use cases
│   │   └── product/
│   │       ├── ProductFactory.kt        # Factory pattern
│   │       ├── ProductService.kt        # Implements ProductServicePort
│   │       └── port/
│   │           └── ProductServicePort.kt # Inbound port (DI)
│   └── adapter/
│       ├── inbound/                      # Driving adapters
│       │   └── rest/
│       │       ├── ProductRestController.kt
│       │       └── ProductValidationAdvice.kt
│       └── outbound/                     # Driven adapters
│           └── persistence/
│               ├── ProductJpaEntity.kt
│               ├── ProductJpaRepository.kt
│               └── ProductRepositoryAdapter.kt
├── src/main/resources/
│   ├── application.yaml
│   └── static/
│       ├── index.html
│       └── products.html
├── build.gradle.kts
└── README.md
```

---

## REST API

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/products` | List all products (cached in Redis) |
| `GET` | `/api/products/search?q={query}` | Search products by name or description (cached in Redis) |
| `GET` | `/api/products/{id}` | Get product by ID |
| `POST` | `/api/products` | Create product |
| `PUT` | `/api/products/{id}` | Update product |
| `DELETE` | `/api/products/{id}` | Delete product |

### Data Model

```json
{
  "id": 1,
  "name": "Product name",
  "description": "Optional description",
  "price": 99.99,
  "stock": 10
}
```

### Validation (ProductFactory)

- **name**: Required, non-blank
- **price**: Must be ≥ 0
- **stock**: Must be ≥ 0 (default: 0)

Invalid data returns `400 Bad Request`.

---

## Prerequisites

- **Java 21** (JDK)
- **PostgreSQL** (database `addi_products`)
- **Redis** (required for product list and search caching)

---

## Configuration

Edit `src/main/resources/application.yaml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/addi_products
    username: addi
    password: addi123

  cache:
    type: redis
    redis:
      time-to-live: 600000
```

---

## Running the Application

```bash
# Create database (if it does not exist)
createdb addi_products

# Run application
./gradlew bootRun
```

**Available URLs:**

| URL | Description |
|-----|-------------|
| http://localhost:8080/ | Home page |
| http://localhost:8080/products | Product CRUD |
| http://localhost:8080/actuator/health | Health status |
| http://localhost:8080/actuator/info | Application info |

---

## Testing

```bash
./gradlew test
```

---

## License

Internal project — ia.dev.codytion
