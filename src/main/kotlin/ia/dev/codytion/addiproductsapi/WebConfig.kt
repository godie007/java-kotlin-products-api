package ia.dev.codytion.addiproductsapi

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig : WebMvcConfigurer {

    /** Redirects /products and /products/ to the products page. */
    override fun addViewControllers(registry: ViewControllerRegistry) {
        registry.addRedirectViewController("/products", "/products.html")
        registry.addRedirectViewController("/products/", "/products.html")
    }
}
