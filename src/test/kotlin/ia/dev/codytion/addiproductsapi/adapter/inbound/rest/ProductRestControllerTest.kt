package ia.dev.codytion.addiproductsapi.adapter.inbound.rest

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import ia.dev.codytion.addiproductsapi.domain.product.Product
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProductRestControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    private val objectMapper = jacksonObjectMapper()

    @Test
    fun `list returns empty array when no products`() {
        mockMvc.perform(get("/api/products"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().string("[]"))
    }

    @Test
    fun `create and list product`() {
        val product = Product(name = "Test Product", description = "Desc", price = 99.99, stock = 10)

        mockMvc.perform(
            post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product))
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.name").value("Test Product"))
            .andExpect(jsonPath("$.price").value(99.99))
            .andExpect(jsonPath("$.id").exists())

        mockMvc.perform(get("/api/products"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].name").value("Test Product"))
    }

    @Test
    fun `getById returns 404 when not found`() {
        mockMvc.perform(get("/api/products/99999"))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `create with invalid data returns 400`() {
        val product = Product(name = "", price = -1.0, stock = -5)

        mockMvc.perform(
            post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product))
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `search returns products when query matches`() {
        val product = Product(name = "UniqueSearchable", description = "X", price = 1.0, stock = 0)
        mockMvc.perform(
            post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product))
        ).andExpect(status().isCreated)

        mockMvc.perform(get("/api/products/search").param("q", "UniqueSearchable"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].name").value("UniqueSearchable"))
    }

    @Test
    fun `delete returns 204 when product exists`() {
        val product = Product(name = "ToDelete", price = 1.0, stock = 0)
        val createResult = mockMvc.perform(
            post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product))
        ).andReturn()
        val created = objectMapper.readValue(createResult.response.contentAsString, Product::class.java)
        val id = created.id!!

        mockMvc.perform(delete("/api/products/$id"))
            .andExpect(status().isNoContent)

        mockMvc.perform(get("/api/products/$id"))
            .andExpect(status().isNotFound)
    }
}
