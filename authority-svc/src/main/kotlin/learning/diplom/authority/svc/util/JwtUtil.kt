package learning.diplom.authority.svc.util

import io.jsonwebtoken.*
import learning.diplom.authority.svc.exception.JwtTokenMalformedException
import learning.diplom.authority.svc.exception.JwtTokenMissingException
import learning.diplom.authority.svc.model.User
import learning.diplom.authority.svc.repository.UserRepository
import learning.diplom.model.error.lib.ServerEntity
import learning.diplom.model.error.lib.exception.rest.EntityNotFoundRestException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.http.ResponseEntity
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component
import java.security.SignatureException
import java.util.*
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse

@Component
class JwtUtil(
    @Value("\${jwt.secret}")
    private val jwtSecret: String? = null,
    @Value("\${jwt.validity}")
    private val tokenValidity: Long = 0,
    private val userRepository: UserRepository,
    private val redisTemplate: RedisTemplate<String, Any>,
    private val bCryptPasswordEncoder: BCryptPasswordEncoder,
    @Value("\${jwt.info}")
    private val info: String? = null
) {

    fun getClaims(token: String?): Claims? {
        try {
            return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).body
        } catch (e: Exception) {
            println(e.message + " => " + e)
        }
        return null
    }

    fun getUserByEmailAndPassword(email: String, password: String): User {
        return if (userRepository.existsByEmailAndPasswordHash(email, password)) {
            userRepository.findByEmailAndPasswordHash(email, password)
        } else {
            throw EntityNotFoundRestException(ServerEntity.USER, "email = $email, password = $password")
        }
    }

    fun getUserByEmailWithPasswordChecking(email: String, password: String): User {
        return if (userRepository.existsByEmail(email)) {
            val user = userRepository.findByEmail(email)
            if(bCryptPasswordEncoder.matches(password, user.passwordHash)) {
                throw EntityNotFoundRestException(ServerEntity.USER, "email = $email, password = $password")
            }
            user
        } else {
            throw EntityNotFoundRestException(ServerEntity.USER, "email = $email, password = $password")
        }
    }
    fun generateToken(email: String?, password: String?, dbUser: User): String? {

        val claims: Claims = Jwts.claims()
            .setSubject(dbUser.email)
        with(dbUser) {
            claims["userId"] = userId
            claims["name"] = name
            claims["surname"] = surname
            claims["patronymic"] = patronymic
            claims["passwordHash"] = passwordHash
            claims["role"] = role
            claims["departmentId"] = departmentId
        }

        val nowMillis = System.currentTimeMillis()
//        val expMillis = nowMillis + tokenValidity
//        val exp = Date(expMillis)
        val exp = Date(Long.MAX_VALUE)
        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(Date(nowMillis)).setExpiration(exp)
            .signWith(SignatureAlgorithm.HS256, jwtSecret).compact()
    }

    fun encryptInfoAndPutItToRedis(email: String, password: String, jwtToken: String): String {
        LOG.info("Put token to redis")
        val redisKey = "$email:$password"
        val encryptedKey = bCryptPasswordEncoder.encode(redisKey)
        redisTemplate.opsForValue().set(encryptedKey, jwtToken)
        return encryptedKey
    }


    fun validateToken(token: String?) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token)
        } catch (ex: SignatureException) {
            throw JwtTokenMalformedException("Invalid JWT signature")
        } catch (ex: MalformedJwtException) {
            throw JwtTokenMalformedException("Invalid JWT token")
        } catch (ex: ExpiredJwtException) {
            throw JwtTokenMalformedException("Expired JWT token")
        } catch (ex: UnsupportedJwtException) {
            throw JwtTokenMalformedException("Unsupported JWT token")
        } catch (ex: IllegalArgumentException) {
            throw JwtTokenMissingException("JWT claims string is empty.")
        }
    }

    fun logout(httpRequest: ServerHttpRequest, httpResponse: HttpServletResponse): ResponseEntity<String> {
        val redisKey = httpRequest.cookies[info]?.get(0)?.value ?: "empty"
        if (redisKey == "empty") {
            return ResponseEntity.notFound().build()
        }
        redisTemplate.opsForValue().getAndDelete(redisKey)
        httpResponse.addCookie(Cookie(redisKey, null))
        return ResponseEntity.ok().build()
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(JwtUtil::class.java)
        private const val KEY = "profile"
    }
}
