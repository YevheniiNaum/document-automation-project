package learning.diplom.user.svc.controller.read

import learning.diplom.user.svc.model.User
import learning.diplom.user.svc.openfeign.client.DocumentSvcClient
import learning.diplom.user.svc.service.UserService
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/user")
class UserReadController(
    private val userService: UserService
) {

    @GetMapping("")
    fun test( ): ResponseEntity<String> {
        return ResponseEntity.ok().body("USER")
    }

    @GetMapping("/{userId}")
    fun getUserById( @PathVariable userId: String, model: Model): String {
        LOG.info("Get user: user = {}", userId)
        val user = userService.getUserByUserId(userId)
        model.addAttribute("user", user)
        return "user"
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(UserReadController::class.java)
    }
}
