package ia.dev.codytion.addiproductsapi.application.product.port

import ia.dev.codytion.addiproductsapi.domain.product.Product

/**
 * Inbound port (use case) for product operations.
 * Defines the application's capabilities.
 * Implemented by ProductService.
 */
interface ProductServicePort {

    fun findAll(): List<Product>
    fun search(query: String): List<Product>
    fun findById(id: Long): Product?
    fun create(product: Product): Product
    fun update(id: Long, product: Product): Product?
    fun delete(id: Long): Boolean
}
