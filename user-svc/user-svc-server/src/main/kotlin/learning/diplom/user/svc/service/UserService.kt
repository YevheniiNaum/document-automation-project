package learning.diplom.user.svc.service

import learning.diplom.department.svc.api.reqresp.user.svc.AddUserToDepartmentRequest
import learning.diplom.model.error.lib.ServerEntity
import learning.diplom.model.error.lib.exception.rest.EntityAlreadyExistsRestException
import learning.diplom.model.error.lib.exception.rest.EntityNotFoundRestException
import learning.diplom.model.error.lib.exception.rest.RestException
import learning.diplom.model.error.lib.mapper.ErrorMapper.mapToRestException
import learning.diplom.model.error.lib.user.svc.Role
import learning.diplom.user.svc.openfeign.client.DepartmentSvcClient
import learning.diplom.user.svc.model.User
import learning.diplom.user.svc.repository.UserRepository
import learning.diplom.user.svc.util.codec.asEnumOf
import learning.diplom.user.svc.util.codec.mapAsEnumOf
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val departmentSvcClient: DepartmentSvcClient,
    private val bCryptPasswordEncoder: BCryptPasswordEncoder
) {
    //// UPDATE METHODS ////
    fun createUser(user: User): User {
        LOG.info("Started creating user: user = {}", user)
        if (userRepository.existsByEmail(user.email!!.toString())) {
            throw EntityAlreadyExistsRestException(ServerEntity.USER, user.toString())
        }

        user.passwordHash = bCryptPasswordEncoder.encode(user.passwordHash.toString())

        val savedUser = userRepository.save(user)
        LOG.info("Started adding user = {}, to department = {}", savedUser.departmentId)

        val addUserToDepartmentResponse = try {
            sendAddUserToDepartmentRequest(user)
        } catch (ex: Exception) {
            LOG.error("ERROR from department-svc: ", ex)
            userRepository.deleteByUserId(savedUser.userId!!)
            throw RestException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal error")
        }

        if (addUserToDepartmentResponse.hasError()) {
            userRepository.deleteByUserId(savedUser.userId!!)
            throw addUserToDepartmentResponse.error.mapToRestException()
        }
        return savedUser
    }

    //// READ METHODS ////
    fun getUserByUserId(userId: String): User {
        return if (userRepository.existsByUserId(userId)) {
            userRepository.findByUserId(userId)
        } else {
            throw EntityNotFoundRestException(ServerEntity.USER, "userId = $userId")
        }
    }

    private fun sendAddUserToDepartmentRequest(user: User) =
        departmentSvcClient.addUserToDepartment(
            user.departmentId!!,
            AddUserToDepartmentRequest.newBuilder()
                .setUserId(user.userId)
                .setName(user.name)
                .setSurname(user.surname)
                .setRole(user.role.asEnumOf())
                .build()
        )

    companion object {
        private val LOG = LoggerFactory.getLogger(UserService::class.java)
    }
}
