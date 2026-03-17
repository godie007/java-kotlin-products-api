package ia.dev.codytion.addiproductsapi.application.product

import ia.dev.codytion.addiproductsapi.domain.product.Product
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ProductFactoryTest {

    private val factory = ProductFactory()

    @Test
    fun `create returns product with null id`() {
        val product = factory.create("Test Product", "Description", 99.99, 10)

        assertNull(product.id)
        assertEquals("Test Product", product.name)
        assertEquals("Description", product.description)
        assertEquals(99.99, product.price)
        assertEquals(10, product.stock)
    }

    @Test
    fun `create trims name and description`() {
        val product = factory.create("  Trimmed  ", "  Desc  ", 0.0, 0)

        assertEquals("Trimmed", product.name)
        assertEquals("Desc", product.description)
    }

    @Test
    fun `create coerces negative stock to zero`() {
        val product = factory.create("Product", null, 10.0, -5)

        assertEquals(0, product.stock)
    }

    @Test
    fun `create throws when name is blank`() {
        assertThrows<IllegalArgumentException> {
            factory.create("", "Desc", 10.0, 5)
        }
        assertThrows<IllegalArgumentException> {
            factory.create("   ", "Desc", 10.0, 5)
        }
    }

    @Test
    fun `create throws when price is negative`() {
        assertThrows<IllegalArgumentException> {
            factory.create("Product", null, -1.0, 5)
        }
    }

    @Test
    fun `createForUpdate returns product with id`() {
        val product = factory.createForUpdate(1L, "Updated", "Desc", 50.0, 20)

        assertEquals(1L, product.id)
        assertEquals("Updated", product.name)
        assertEquals(50.0, product.price)
    }

    @Test
    fun `fromRequest validates and creates product`() {
        val input = Product(name = "From Request", description = "Test", price = 25.0, stock = 3)
        val product = factory.fromRequest(input)

        assertNull(product.id)
        assertEquals("From Request", product.name)
        assertEquals(25.0, product.price)
    }

    @Test
    fun `fromRequestForUpdate validates and creates product with id`() {
        val input = Product(name = "Updated", price = 100.0, stock = 15)
        val product = factory.fromRequestForUpdate(42L, input)

        assertEquals(42L, product.id)
        assertEquals("Updated", product.name)
        assertEquals(100.0, product.price)
    }
}
