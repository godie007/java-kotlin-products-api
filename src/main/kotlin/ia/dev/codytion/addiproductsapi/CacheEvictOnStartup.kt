package ia.dev.codytion.addiproductsapi

import org.slf4j.LoggerFactory
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.cache.CacheManager
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

/**
 * Clears product caches on startup to avoid deserialization errors
 * when domain classes have been refactored (e.g., package/class rename).
 */
@Component
@Order(Int.MIN_VALUE)
class CacheEvictOnStartup(private val cacheManager: CacheManager) : ApplicationRunner {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun run(args: ApplicationArguments) {
        listOf("products", "product-search").forEach { cacheName ->
            cacheManager.getCache(cacheName)?.clear()
            log.info("Cleared cache: $cacheName")
        }
    }
}
