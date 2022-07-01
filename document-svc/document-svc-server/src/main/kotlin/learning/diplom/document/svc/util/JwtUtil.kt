package learning.diplom.document.svc.util

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import learning.diplom.document.svc.model.Role
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

    fun getRoleWithApproveFieldFromClaims(claims: Claims): Pair<Role, String>?{
        return claims["role"].toString().mapClaimRole()
    }

    fun getJwtToken(redisKey: String?): Any? {
        return redisTemplate.opsForValue().get(redisKey ?: "empty")
    }

    private fun String.mapClaimRole(): Pair<Role, String>? {
        return when {
            equals("HEAD_OF_ADMINISTRATION") -> Role.HEAD_OF_ADMINISTRATION to "headOfAdministrationApprove"
            equals("DEPUTY_HEAD_OF_ADMINISTRATION") -> Role.DEPUTY_HEAD_OF_ADMINISTRATION to "deputyHeadOfAdministrationApprove"
            equals("HEAD_OF_DEPARTMENT") -> Role.HEAD_OF_DEPARTMENT to "headOfDepartmentApprove"
            equals("DEPUTY_HEAD_OF_DEPARTMENT") -> Role.DEPUTY_HEAD_OF_DEPARTMENT to "deputyHeadOfDepartmentApprove"
            equals("EMPLOYEE") -> Role.EMPLOYEE to "employeeApprove"

            else -> null
        }
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(JwtUtil::class.java)
    }

}
