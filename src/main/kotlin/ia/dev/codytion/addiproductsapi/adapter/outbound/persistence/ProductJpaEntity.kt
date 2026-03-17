package ia.dev.codytion.addiproductsapi.adapter.outbound.persistence

import ia.dev.codytion.addiproductsapi.domain.product.Product
import jakarta.persistence.*
import java.io.Serializable

/**
 * JPA entity for product persistence.
 * Maps domain Product to database table.
 */
@Entity
@Table(name = "products")
data class ProductJpaEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    var name: String,

    @Column(length = 1000)
    var description: String? = null,

    @Column(nullable = false)
    var price: Double,

    @Column(nullable = false)
    var stock: Int = 0
) : Serializable {

    fun toDomain(): Product = Product(
        id = id,
        name = name,
        description = description,
        price = price,
        stock = stock
    )

    companion object {
        fun fromDomain(product: Product): ProductJpaEntity = ProductJpaEntity(
            id = product.id,
            name = product.name,
            description = product.description,
            price = product.price,
            stock = product.stock
        )
    }
}
