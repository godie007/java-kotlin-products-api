package ia.dev.codytion.addiproductsapi.product

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

/** JPA repository for Product entity. Provides CRUD operations against PostgreSQL. */
interface ProductRepository : JpaRepository<Product, Long> {

    /** Searches products by name or description (case-insensitive). */
    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(p.description) LIKE LOWER(CONCAT('%', :query, '%'))")
    fun search(@Param("query") query: String): List<Product>
}
