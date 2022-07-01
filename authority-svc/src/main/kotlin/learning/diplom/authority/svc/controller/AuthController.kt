package learning.diplom.authority.svc.controller

import learning.diplom.authority.svc.model.LoginRequest
import learning.diplom.authority.svc.util.JwtUtil
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import java.net.URI
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse

@Controller
@RequestMapping("/auth")
class AuthController(
    private val jwtUtil: JwtUtil,
    @Value("\${jwt.info}")
    private val info: String? = null,
    @Value("\${gateway.host}")
    private val host: String? = null,
    @Value("\${gateway.port}")
    private val port: String? = null,
    private val redisTemplate: RedisTemplate<String, Any>
) {

    @PostMapping("/login")
    fun login(
        @ModelAttribute @RequestBody request: LoginRequest,
        httpResponse: HttpServletResponse
    ): ResponseEntity<String> {
        val dbUser = jwtUtil.getUserByEmailWithPasswordChecking(request.email!!, request.password!!)
        LOG.info("Generate token for request = {}", request)
        val jwtToken = jwtUtil.generateToken(request.email, request.password, dbUser)
        val encryptedKey = jwtUtil.encryptInfoAndPutItToRedis(request.email, request.password, jwtToken!!)
        LOG.info("Generating cookie")
        val cookie = Cookie(info, encryptedKey)
        cookie.path = "/"
        httpResponse.addCookie(cookie)

        return ResponseEntity
            .status(HttpStatus.FOUND)
            .location(URI("$HTTP_PREFIX$host:$port/home"))
            .build()
    }

    @PostMapping("/logout")
    fun logout(
        httpResponse: HttpServletResponse,
        @CookieValue("info") redisKey: String? = null,
        model: Model
    ): String {

        if (redisKey.isNullOrBlank()) {
            throw InternalError("NotFound info")
        }
        redisTemplate.delete(redisKey)
        model.addAttribute("loginRequest", LoginRequest())
        return "login-form"

    }

    @GetMapping("/login")
    fun getLoginForm(model: Model): String? {
        model.addAttribute("loginRequest", LoginRequest())
        return "login-form"
    }

    @GetMapping("/logout")
    fun getLogoutForm(): String {
        return "logout-form"
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(AuthController::class.java)
        private const val HTTP_PREFIX = "http://"
    }
}
