package learning.diplom.user.svc.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
class RedisConfig {
    @Bean
    fun redisTemplate(connectionFactory: RedisConnectionFactory): RedisTemplate<String, Any> {
        return RedisTemplate<String, Any>().apply {
            setConnectionFactory(connectionFactory)
            val stringRedisSerializer = StringRedisSerializer()
            val jsonSerializer = GenericJackson2JsonRedisSerializer()
            keySerializer = stringRedisSerializer
            valueSerializer = jsonSerializer
            hashKeySerializer = stringRedisSerializer
            hashValueSerializer = jsonSerializer
            setEnableTransactionSupport(true)
        }
    }
}
