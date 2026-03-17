# Addi Products API

**Product Management API** — Enterprise solution for product catalog and administration with PostgreSQL persistence, caching, and integrated web interface.

---

## Executive Summary

Addi Products API is a backend application built with **Kotlin** and **Spring Boot 4** that exposes REST services for product CRUD operations. It includes an integrated web interface for direct administration and supports configurable caching (in-memory or Redis) to optimize query performance.

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
                    │  │  ProductService (+ Redis cache)                  │     │
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

## Technology Stack

| Technology | Purpose |
|------------|---------|
| **Kotlin 2.2** | Primary language |
| **Spring Boot 4** | Web and backend framework |
| **Spring Data JPA** | Persistence and data access |
| **PostgreSQL** | Relational database |
| **Spring Cache** | Query caching (memory or Redis) |
| **Spring Data Redis** | Distributed cache (optional) |
| **Spring Actuator** | Monitoring and metrics |

---

## Project Structure (Hexagonal)

```
addi-products-api/
├── src/main/kotlin/ia/dev/codytion/addiproductsapi/
│   ├── AddiProductsApiApplication.kt
│   ├── WebConfig.kt
│   ├── domain/                          # Core business logic
│   │   └── product/
│   │       ├── Product.kt                # Domain entity
│   │       └── port/
│   │           └── ProductRepositoryPort.kt
│   ├── application/                     # Use cases
│   │   └── product/
│   │       └── ProductService.kt
│   └── adapter/
│       ├── inbound/                      # Driving adapters
│       │   └── rest/
│       │       └── ProductRestController.kt
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
