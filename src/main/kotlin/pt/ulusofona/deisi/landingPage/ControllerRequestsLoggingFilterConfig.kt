package pt.ulusofona.deisi.landingPage

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.dropProject.filters.ControllerRequestsLoggingFilter


@Configuration
class ControllerRequestsLoggingFilterConfig {

    // ********** IMPORTANT **********
    // don't forget to include the following line in landing-page.properties:
    // logging.level.org.springframework.web.filter.ControllerRequestsLoggingFilter=INFO

    @Bean
    fun logFilter(): ControllerRequestsLoggingFilter {
        val filter = ControllerRequestsLoggingFilter()
        return filter
    }
}
