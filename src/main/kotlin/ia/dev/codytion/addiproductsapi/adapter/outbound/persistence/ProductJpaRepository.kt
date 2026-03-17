package ia.dev.codytion.addiproductsapi.adapter.outbound.persistence

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

/** Spring Data JPA repository. Internal to the persistence adapter. */
interface ProductJpaRepository : JpaRepository<ProductJpaEntity, Long> {

    @Query(
        "SELECT p FROM ProductJpaEntity p WHERE " +
        "LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
        "LOWER(p.description) LIKE LOWER(CONCAT('%', :query, '%'))"
    )
    fun search(@Param("query") query: String): List<ProductJpaEntity>
}
