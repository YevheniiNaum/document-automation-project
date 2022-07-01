//package learning.diplom.eurekaserver.config
//
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.context.annotation.Configuration
//import org.springframework.core.annotation.Order
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
//import org.springframework.security.config.annotation.web.builders.HttpSecurity
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
//import org.springframework.security.config.http.SessionCreationPolicy
//
//@Configuration
//@EnableWebSecurity
//@Order(1)
//class SecurityConfig : WebSecurityConfigurerAdapter(){
//
//    @Autowired
//    fun configureGlobal(auth: AuthenticationManagerBuilder) {
//        auth.inMemoryAuthentication()
//            .withUser("discUser").password("discPassword").roles("SYSTEM")
//    }
//
//    override fun configure(auth: AuthenticationManagerBuilder) {
//        auth.inMemoryAuthentication()
//            .withUser("user").password("password").roles("USER")
//            .and()
//            .withUser("admin").password("{noop}password").roles("USER", "ADMIN")
//    }
//
//    override fun configure(http: HttpSecurity) {
//        http.sessionManagement()
//            .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
//            .and().requestMatchers().antMatchers("/eureka/**")
//            .and().authorizeRequests().antMatchers("/eureka/**")
//            .hasRole("SYSTEM").anyRequest().denyAll().and()
//            .httpBasic().and().csrf().disable()
//    }
//}
