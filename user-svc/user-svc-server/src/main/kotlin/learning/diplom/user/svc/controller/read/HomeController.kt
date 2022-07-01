package learning.diplom.user.svc.controller.read

import learning.diplom.user.svc.repository.UserRepository
import learning.diplom.user.svc.service.UserService
import learning.diplom.user.svc.util.JwtUtil
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/home")
class HomeController(
    private val jwtUtil: JwtUtil,
    private val userService: UserService
) {

    @GetMapping("")
    fun getHomePage(@CookieValue("info") redisKey: String, model: Model): String {
        val claims = jwtUtil.getClaims(redisKey)!!
        val id = claims["userId"].toString()
        val user = userService.getUserByUserId(id)
        model.addAttribute("user", user)
        return "home"
    }
}
