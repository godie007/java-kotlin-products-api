package ia.dev.codytion.addiproductsapi.domain.product

import java.io.Serializable

/**
 * Domain entity representing a product in the catalog.
 * Pure domain model with no framework dependencies.
 *
 * @property id Auto-generated primary key (null for new products)
 * @property name Product name (required)
 * @property description Optional product description
 * @property price Product price
 * @property stock Available stock quantity
 */
data class Product(
    val id: Long? = null,
    val name: String,
    val description: String? = null,
    val price: Double,
    val stock: Int = 0
) : Serializable
