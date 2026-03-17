package ia.dev.codytion.addiproductsapi.adapter.inbound.rest

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

/**
 * Handles validation exceptions from ProductFactory.
 * Returns 400 Bad Request for invalid product data.
 */
@RestControllerAdvice
class ProductValidationAdvice {

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleValidationError(ex: IllegalArgumentException): ResponseEntity<Map<String, String>> =
        ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(mapOf("error" to (ex.message ?: "Validation failed")))
}
