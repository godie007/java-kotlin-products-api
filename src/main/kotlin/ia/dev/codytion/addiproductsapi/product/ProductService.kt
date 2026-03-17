package ia.dev.codytion.addiproductsapi.product

import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

/**
 * Service layer for product operations.
 * Product list (findAll) and search (search) are cached in Redis. All other operations use direct DB access.
 */
@Service
class ProductService(private val productRepository: ProductRepository) {

    /** Returns all products. Cached in Redis. */
    @Cacheable(value = ["products"], key = "'list'")
    fun findAll(): List<Product> = productRepository.findAll()

    /** Searches products by name or description. Cached in Redis per query. */
    @Cacheable(value = ["product-search"], key = "#query")
    fun search(query: String): List<Product> = productRepository.search(query)

    /** Returns a product by ID, or null if not found. Direct DB read. */
    fun findById(id: Long): Product? = productRepository.findById(id).orElse(null)

    /** Creates a new product. Direct DB write, evicts caches. */
    @CacheEvict(value = ["products", "product-search"], allEntries = true)
    fun create(product: Product): Product = productRepository.save(product)

    /** Updates an existing product. Direct DB write, evicts caches. */
    @CacheEvict(value = ["products", "product-search"], allEntries = true)
    fun update(id: Long, product: Product): Product? {
        return if (productRepository.existsById(id)) {
            productRepository.save(product.copy(id = id))
        } else null
    }

    /** Deletes a product by ID. Direct DB delete, evicts caches. */
    @CacheEvict(value = ["products", "product-search"], allEntries = true)
    fun delete(id: Long): Boolean = if (productRepository.existsById(id)) {
        productRepository.deleteById(id)
        true
    } else false
}
