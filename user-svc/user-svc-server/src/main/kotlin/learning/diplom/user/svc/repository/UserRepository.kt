package learning.diplom.user.svc.repository

import learning.diplom.user.svc.model.User
import org.springframework.data.mongodb.repository.MongoRepository

interface UserRepository: MongoRepository<User, String> {
    fun deleteByUserId(userId: String)
    fun findByUserId (userId: String): User
    fun findByName(firstName: String): User
    fun findByEmail(email: String): User
    fun existsByEmail(email: String): Boolean
    fun existsByUserId(userId: String): Boolean
}
