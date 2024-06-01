package by.bsuir.jenalyon.cors

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
class CorsConfig {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        try {
            return http
                .cors(Customizer.withDefaults())
                .httpBasic { it.disable() }
                .csrf { it.disable() }
                .authorizeHttpRequests {
                    it.requestMatchers("/**").permitAll()
                }
                .build()
        } catch (e: Exception) {
            throw IllegalStateException(e)
        }
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()

        configuration.allowedOriginPatterns = listOf("*")
        configuration.allowedMethods =
            listOf("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
        configuration.allowedHeaders = listOf("*")

        val source: UrlBasedCorsConfigurationSource = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)

        return source
    }


}