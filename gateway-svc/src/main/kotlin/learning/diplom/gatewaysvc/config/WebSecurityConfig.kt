package learning.diplom.gatewaysvc.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain

@Configuration
class WebSecurityConfig {
    @Bean
    fun springSecurityFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        http.csrf().disable()
        http.authorizeExchange()
            .pathMatchers("/test-admin").hasAnyRole("ADMIN")
            .pathMatchers("/test-user").hasRole("USER")
            .pathMatchers("/login**", "/test-public").permitAll()
            .anyExchange()
            .authenticated()
            .and()
            .oauth2Login()
        return http.build()
    }
}
