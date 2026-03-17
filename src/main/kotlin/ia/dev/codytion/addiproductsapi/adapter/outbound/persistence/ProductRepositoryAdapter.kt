package ia.dev.codytion.addiproductsapi.adapter.outbound.persistence

import ia.dev.codytion.addiproductsapi.domain.product.Product
import ia.dev.codytion.addiproductsapi.domain.product.port.ProductRepositoryPort
import org.springframework.stereotype.Component

/**
 * Outbound adapter for product persistence.
 * Implements ProductRepositoryPort using JPA/PostgreSQL.
 */
@Component
class ProductRepositoryAdapter(
    private val jpaRepository: ProductJpaRepository
) : ProductRepositoryPort {

    override fun findAll(): List<Product> =
        jpaRepository.findAll().map { it.toDomain() }

    override fun findById(id: Long): Product? =
        jpaRepository.findById(id).map { it.toDomain() }.orElse(null)

    override fun existsById(id: Long): Boolean =
        jpaRepository.existsById(id)

    override fun save(product: Product): Product =
        jpaRepository.save(ProductJpaEntity.fromDomain(product)).toDomain()

    override fun deleteById(id: Long) {
        jpaRepository.deleteById(id)
    }

    override fun search(query: String): List<Product> =
        jpaRepository.search(query).map { it.toDomain() }
}
