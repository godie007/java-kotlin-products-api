package ia.dev.codytion.addiproductsapi.product

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * REST controller for product CRUD operations.
 * Exposes endpoints under /api/products.
 */
@RestController
@RequestMapping("/api/products")
class ProductApiController(private val productService: ProductService) {

    /** Returns all products. Results are cached in Redis. */
    @GetMapping
    fun list(): List<Product> = productService.findAll()

    /** Searches products by name or description. Results are cached in Redis. */
    @GetMapping("/search")
    fun search(@RequestParam(defaultValue = "") q: String): List<Product> =
        if (q.isBlank()) productService.findAll() else productService.search(q.trim())

    /** Returns a single product by ID, or 404 if not found. */
    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<Product> =
        productService.findById(id)?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build()

    /** Creates a new product. Returns 201 Created. */
    @PostMapping
    fun create(@RequestBody product: Product): ResponseEntity<Product> =
        ResponseEntity.status(HttpStatus.CREATED).body(productService.create(product))

    /** Updates an existing product. Returns 404 if not found. */
    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody product: Product): ResponseEntity<Product> =
        productService.update(id, product)?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build()

    /** Deletes a product. Returns 204 No Content on success, 404 if not found. */
    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Void> =
        if (productService.delete(id)) ResponseEntity.noContent().build()
        else ResponseEntity.notFound().build()
}
