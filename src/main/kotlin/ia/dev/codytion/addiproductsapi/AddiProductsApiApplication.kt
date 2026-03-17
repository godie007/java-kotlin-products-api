package ia.dev.codytion.addiproductsapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching

/** Main application entry point. Enables Spring Boot caching for product queries. */
@SpringBootApplication
@EnableCaching
class AddiProductsApiApplication

fun main(args: Array<String>) {
    runApplication<AddiProductsApiApplication>(*args)
}
