package learning.diplom.user.svc.util

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import learning.diplom.model.error.lib.ServerEntity
import learning.diplom.model.error.lib.exception.rest.EntityNotFoundRestException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component

@Component
class JwtUtil(
    @Value("\${jwt.secret}")
    private val jwtSecret: String? = null,
    @Value("\${jwt.validity}")
    private val tokenValidity: Long = 0,
    @Value("\${jwt.info}")
    private val info: String? = null,
    private val redisTemplate: RedisTemplate<String, Any>
) {

    fun getClaims(redisKey: String?): Claims? {
        val token = getJwtToken(redisKey)
        if (redisKey.isNullOrBlank() || token == null) {
            LOG.error("Token did not find")
            throw EntityNotFoundRestException(ServerEntity.TOKEN, "redisKey = $redisKey")
        }
        try {
            return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token.toString()).body
        } catch (e: Exception) {
            println(e.message + " => " + e)
        }
        return null
    }

    fun getJwtToken(redisKey: String?): Any? {
        return redisTemplate.opsForValue().get(redisKey ?: "empty")
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(JwtUtil::class.java)
    }

}
