package pt.ulusofona.deisi.landingPage;

import io.imagekit.sdk.ImageKit
import io.imagekit.sdk.utils.Utils
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer
import org.springframework.context.annotation.PropertySource

@SpringBootApplication
@PropertySource("classpath:landing-page.properties")
class DEISILandingPageApplication : SpringBootServletInitializer() {

    override fun configure(application: SpringApplicationBuilder): SpringApplicationBuilder {
        return application.sources(DEISILandingPageApplication::class.java).properties("spring.config.name: landing-page")
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            System.setProperty("spring.config.name", "landing-page")
            SpringApplication.run(DEISILandingPageApplication::class.java, *args)

            val imageKit = ImageKit.getInstance()
            val config = Utils.getSystemConfig(DEISILandingPageApplication::class.java)
            imageKit.config = config
        }
    }
}
