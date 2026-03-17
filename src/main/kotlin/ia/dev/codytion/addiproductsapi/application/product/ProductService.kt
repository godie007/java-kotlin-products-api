package ia.dev.codytion.addiproductsapi.application.product

import ia.dev.codytion.addiproductsapi.domain.product.Product
import ia.dev.codytion.addiproductsapi.domain.product.port.ProductRepositoryPort
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

/**
 * Application service (use case) for product operations.
 * Orchestrates domain logic and uses outbound ports.
 * Product list and search are cached in Redis.
 */
@Service
class ProductService(private val productRepository: ProductRepositoryPort) {

    @Cacheable(value = ["products"], key = "'list'")
    fun findAll(): List<Product> = productRepository.findAll()

    @Cacheable(value = ["product-search"], key = "#query")
    fun search(query: String): List<Product> = productRepository.search(query)

    fun findById(id: Long): Product? = productRepository.findById(id)

    @CacheEvict(value = ["products", "product-search"], allEntries = true)
    fun create(product: Product): Product = productRepository.save(product)

    @CacheEvict(value = ["products", "product-search"], allEntries = true)
    fun update(id: Long, product: Product): Product? =
        if (productRepository.existsById(id)) {
            productRepository.save(product.copy(id = id))
        } else null

    @CacheEvict(value = ["products", "product-search"], allEntries = true)
    fun delete(id: Long): Boolean =
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id)
            true
        } else false
}
