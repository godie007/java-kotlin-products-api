package ia.dev.codytion.addiproductsapi.application.product

import ia.dev.codytion.addiproductsapi.domain.product.Product
import ia.dev.codytion.addiproductsapi.domain.product.port.ProductRepositoryPort
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ProductServiceTest {

    private val productRepository = mockk<ProductRepositoryPort>()
    private val productFactory = ProductFactory()
    private val productService = ProductService(productRepository, productFactory)

    @Test
    fun `findAll returns list from repository`() {
        val products = listOf(
            Product(1L, "A", null, 10.0, 5),
            Product(2L, "B", "Desc", 20.0, 10)
        )
        every { productRepository.findAll() } returns products

        val result = productService.findAll()

        assertEquals(2, result.size)
        assertEquals("A", result[0].name)
        verify(exactly = 1) { productRepository.findAll() }
    }

    @Test
    fun `findById returns product when exists`() {
        val product = Product(1L, "Product", null, 99.99, 1)
        every { productRepository.findById(1L) } returns product

        val result = productService.findById(1L)

        assertNotNull(result)
        assertEquals(1L, result?.id)
        assertEquals("Product", result?.name)
    }

    @Test
    fun `findById returns null when not exists`() {
        every { productRepository.findById(999L) } returns null

        val result = productService.findById(999L)

        assertNull(result)
    }

    @Test
    fun `create saves validated product`() {
        val input = Product(name = "New", description = "Desc", price = 50.0, stock = 3)
        val saved = Product(1L, "New", "Desc", 50.0, 3)
        every { productRepository.save(any()) } returns saved

        val result = productService.create(input)

        assertEquals(1L, result.id)
        assertEquals("New", result.name)
        verify(exactly = 1) { productRepository.save(any()) }
    }

    @Test
    fun `update returns null when product not exists`() {
        every { productRepository.existsById(999L) } returns false

        val result = productService.update(999L, Product(name = "X", price = 1.0, stock = 0))

        assertNull(result)
        verify(exactly = 0) { productRepository.save(any()) }
    }

    @Test
    fun `update saves when product exists`() {
        val input = Product(name = "Updated", price = 100.0, stock = 5)
        val saved = Product(1L, "Updated", null, 100.0, 5)
        every { productRepository.existsById(1L) } returns true
        every { productRepository.save(any()) } returns saved

        val result = productService.update(1L, input)

        assertNotNull(result)
        assertEquals("Updated", result?.name)
        verify(exactly = 1) { productRepository.save(any()) }
    }

    @Test
    fun `delete returns false when product not exists`() {
        every { productRepository.existsById(999L) } returns false

        val result = productService.delete(999L)

        assertFalse(result)
        verify(exactly = 0) { productRepository.deleteById(any()) }
    }

    @Test
    fun `delete returns true and calls delete when exists`() {
        every { productRepository.existsById(1L) } returns true
        every { productRepository.deleteById(1L) } just Runs

        val result = productService.delete(1L)

        assertTrue(result)
        verify(exactly = 1) { productRepository.deleteById(1L) }
    }

    @Test
    fun `search delegates to repository`() {
        val products = listOf(Product(1L, "Searchable", null, 10.0, 1))
        every { productRepository.search("search") } returns products

        val result = productService.search("search")

        assertEquals(1, result.size)
        assertEquals("Searchable", result[0].name)
        verify(exactly = 1) { productRepository.search("search") }
    }
}
