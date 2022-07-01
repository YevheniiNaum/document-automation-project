package learning.diplom.department.svc.config

import learning.diplom.model.error.lib.handler.ConstraintViolationExceptionHandler
import learning.diplom.model.error.lib.handler.RequestBodyValidationExceptionHandler
import learning.diplom.model.error.lib.handler.RestExceptionHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Configuration
class BeanConfig {
    @Bean
    fun bCryptPasswordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun restExceptionHandler(): RestExceptionHandler {
        return RestExceptionHandler()
    }

    @Bean
    fun constraintViolationExceptionHandler(): ConstraintViolationExceptionHandler {
        return ConstraintViolationExceptionHandler()
    }

//    @Bean
//    fun requestBodyValidationExceptionHandler(): RequestBodyValidationExceptionHandler {
//        return RequestBodyValidationExceptionHandler()
//    }
}

