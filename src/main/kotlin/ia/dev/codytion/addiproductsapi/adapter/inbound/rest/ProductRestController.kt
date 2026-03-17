package ia.dev.codytion.addiproductsapi.adapter.inbound.rest

import ia.dev.codytion.addiproductsapi.application.product.ProductService
import ia.dev.codytion.addiproductsapi.domain.product.Product
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * Inbound adapter (primary/driving) for product REST API.
 * Exposes HTTP endpoints and delegates to application layer.
 */
@RestController
@RequestMapping("/api/products")
class ProductRestController(private val productService: ProductService) {

    @GetMapping
    fun list(): List<Product> = productService.findAll()

    @GetMapping("/search")
    fun search(@RequestParam(defaultValue = "") q: String): List<Product> =
        if (q.isBlank()) productService.findAll() else productService.search(q.trim())

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<Product> =
        productService.findById(id)?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build()

    @PostMapping
    fun create(@RequestBody product: Product): ResponseEntity<Product> =
        ResponseEntity.status(HttpStatus.CREATED).body(productService.create(product))

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody product: Product): ResponseEntity<Product> =
        productService.update(id, product)?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build()

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Void> =
        if (productService.delete(id)) ResponseEntity.noContent().build()
        else ResponseEntity.notFound().build()
}
