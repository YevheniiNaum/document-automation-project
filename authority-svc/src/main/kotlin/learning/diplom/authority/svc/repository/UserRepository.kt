package learning.diplom.authority.svc.repository

import learning.diplom.authority.svc.model.User
import org.springframework.data.mongodb.repository.MongoRepository

interface UserRepository: MongoRepository<User, String> {
    fun findByEmailAndPasswordHash(email: String, passwordHash: String): User
    fun existsByEmailAndPasswordHash(email: String, passwordHash: String): Boolean
    fun findByEmail(email: String): User
    fun existsByEmail(email: String): Boolean
}
