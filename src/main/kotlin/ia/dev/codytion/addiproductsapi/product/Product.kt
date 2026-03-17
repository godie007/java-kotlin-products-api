package ia.dev.codytion.addiproductsapi.product

import jakarta.persistence.*
import java.io.Serializable

/**
 * JPA entity representing a product in the catalog.
 *
 * @property id Auto-generated primary key (null for new products)
 * @property name Product name (required)
 * @property description Optional product description
 * @property price Product price
 * @property stock Available stock quantity
 */
@Entity
@Table(name = "products")
data class Product(
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
) : Serializable
