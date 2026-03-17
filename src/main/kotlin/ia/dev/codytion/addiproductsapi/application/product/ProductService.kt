package ia.dev.codytion.addiproductsapi.application.product

import ia.dev.codytion.addiproductsapi.application.product.port.ProductServicePort
import ia.dev.codytion.addiproductsapi.domain.product.Product
import ia.dev.codytion.addiproductsapi.application.product.ProductFactory
import ia.dev.codytion.addiproductsapi.domain.product.port.ProductRepositoryPort
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

/**
 * Application service (use case) for product operations.
 * Implements ProductServicePort. Uses Factory for creation and Repository port for persistence.
 * Dependencies are injected via constructor (Dependency Injection).
 */
@Service
class ProductService(
    private val productRepository: ProductRepositoryPort,
    private val productFactory: ProductFactory
) : ProductServicePort {

    @Cacheable(value = ["products"], key = "'list'")
    override fun findAll(): List<Product> = productRepository.findAll()

    @Cacheable(value = ["product-search"], key = "#query")
    override fun search(query: String): List<Product> = productRepository.search(query)

    override fun findById(id: Long): Product? = productRepository.findById(id)

    @CacheEvict(value = ["products", "product-search"], allEntries = true)
    override fun create(product: Product): Product {
        val validated = productFactory.fromRequest(product)
        return productRepository.save(validated)
    }

    @CacheEvict(value = ["products", "product-search"], allEntries = true)
    override fun update(id: Long, product: Product): Product? =
        if (productRepository.existsById(id)) {
            val validated = productFactory.fromRequestForUpdate(id, product)
            productRepository.save(validated)
        } else null

    @CacheEvict(value = ["products", "product-search"], allEntries = true)
    override fun delete(id: Long): Boolean =
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id)
            true
        } else false
}
