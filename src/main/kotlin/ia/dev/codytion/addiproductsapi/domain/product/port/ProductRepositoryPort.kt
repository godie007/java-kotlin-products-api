package ia.dev.codytion.addiproductsapi.domain.product.port

import ia.dev.codytion.addiproductsapi.domain.product.Product

/**
 * Outbound port for product persistence.
 * Defines the contract for storing and retrieving products.
 * Implemented by persistence adapters (e.g., JPA, MongoDB).
 */
interface ProductRepositoryPort {

    fun findAll(): List<Product>
    fun findById(id: Long): Product?
    fun existsById(id: Long): Boolean
    fun save(product: Product): Product
    fun deleteById(id: Long)
    fun search(query: String): List<Product>
}
