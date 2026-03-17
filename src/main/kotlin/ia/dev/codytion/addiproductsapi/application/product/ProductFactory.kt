package ia.dev.codytion.addiproductsapi.application.product

import ia.dev.codytion.addiproductsapi.domain.product.Product
import org.springframework.stereotype.Component

/**
 * Factory for creating Product domain entities.
 * Encapsulates creation logic and validation (Factory pattern).
 */
@Component
class ProductFactory {

    /**
     * Creates a new Product (id = null).
     * @throws IllegalArgumentException if validation fails
     */
    fun create(
        name: String,
        description: String? = null,
        price: Double,
        stock: Int = 0
    ): Product {
        validate(name, description, price, stock)
        return Product(
            id = null,
            name = name.trim(),
            description = description?.trim()?.takeIf { it.isNotEmpty() },
            price = price,
            stock = stock.coerceAtLeast(0)
        )
    }

    /**
     * Creates a Product for update (with existing id).
     */
    fun createForUpdate(
        id: Long,
        name: String,
        description: String? = null,
        price: Double,
        stock: Int = 0
    ): Product {
        validate(name, description, price, stock)
        return Product(
            id = id,
            name = name.trim(),
            description = description?.trim()?.takeIf { it.isNotEmpty() },
            price = price,
            stock = stock.coerceAtLeast(0)
        )
    }

    /**
     * Creates Product from existing Product (e.g., from API request).
     * Validates and normalizes data.
     */
    fun fromRequest(product: Product): Product = create(
        name = product.name,
        description = product.description,
        price = product.price,
        stock = product.stock
    )

    /**
     * Creates Product for update from existing Product.
     */
    fun fromRequestForUpdate(id: Long, product: Product): Product = createForUpdate(
        id = id,
        name = product.name,
        description = product.description,
        price = product.price,
        stock = product.stock
    )

    private fun validate(name: String, description: String?, price: Double, stock: Int) {
        require(name.isNotBlank()) { "Product name is required" }
        require(price >= 0) { "Price must be non-negative" }
        require(stock >= 0) { "Stock must be non-negative" }
    }
}
