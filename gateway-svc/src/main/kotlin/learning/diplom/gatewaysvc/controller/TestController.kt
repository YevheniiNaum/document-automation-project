package learning.diplom.gatewaysvc.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping
class TestController {

    @GetMapping("/test-public")
    fun getTestPublic():String{
        return "PUBLIC"
    }

    @GetMapping("/test-admin")
    fun getTestAdmin(): String {
        return "ADMIN"
    }

    @GetMapping("/test-user")
    fun getTestUser(): String {
        return "USER"
    }
}
