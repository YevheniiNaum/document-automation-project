package learning.diplom.user.svc.service

import assertk.all
import assertk.assertThat
import assertk.assertions.*
import learning.diplom.model.error.lib.Entity
import learning.diplom.model.error.lib.exception.proto.ProtoErrorUtil.createEntityNotFoundErrorBuilder
import learning.diplom.model.error.lib.exception.rest.RestException
import learning.diplom.user.svc.AbstractServiceIT
import learning.diplom.user.svc.fixture.UserFixture.randomDbUser
import learning.diplom.user.svc.fixture.request.AddUserToDepartmentRequestFixture.addUserToDepartmentRequest
import learning.diplom.user.svc.model.User
import org.bson.types.ObjectId
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus

internal class UserServiceIT : AbstractServiceIT() {

    @Test
    fun shouldCreateUser() {
        // GIVEN
        val user = randomDbUser()

        val addUserToDepartmentRequest = addUserToDepartmentRequest(user)
        departmentSvcClientStub.mockAddUserToDepartmentSuccessResponse(
            user.departmentId!!,
            addUserToDepartmentRequest
        )

        // WHEN
        val result = userService.createUser(user)

        // THEN
        assertThat(result).isEqualToIgnoringGivenProperties(user, *userFieldsToIgnoring.toTypedArray())
    }

    @Test
    fun shouldFailCreateUserIfItAlreadyExists() {
        // GIVEN
        val dbUser = createDbUser()
        val newUser = randomDbUser().copy(email = dbUser.email)

        // WHEN-THEN
        assertThat {
            userService.createUser(newUser)
        }
            .isFailure()
            .isInstanceOf(RestException::class.java)
            .all {
                prop(RestException::statusCode).isEqualTo(HttpStatus.CONFLICT)
                prop(RestException::message).contains(USER_ALREADY_EXISTS_ERROR_MESSAGE)
            }
    }

    @Test
    fun shouldFailCreateUserIfDepartmentSentError() {
        // GIVEN
        val user = randomDbUser()
        val addUserToDepartmentRequest = addUserToDepartmentRequest(user)

        val errorResponse = createEntityNotFoundErrorBuilder(Entity.DEPARTMENT, "error test").build()

        departmentSvcClientStub.mockAddUserToDepartmentErrorResponse(
            user.departmentId!!,
            addUserToDepartmentRequest,
            errorResponse
        )

        // WHEN-THEN
        assertThat {
            userService.createUser(user)
        }
            .isFailure()
            .isInstanceOf(RestException::class.java)
            .all {
                prop(RestException::statusCode).isEqualTo(HttpStatus.NOT_FOUND)
                prop(RestException::message).contains(DEPARTMENT_NOT_FOUND_ERROR_MESSAGE)
                prop(RestException::message).contains("error test")
            }
    }

    @Test
    fun shouldGetUserByUserId() {
        // GIVEN
        val dbUser = createDbUser()

        // WHEN
        val actual = userService.getUserByUserId(dbUser.userId!!)

        // THEN
        assertThat(actual).isEqualToIgnoringGivenProperties(dbUser, *userFieldsToIgnoring.toTypedArray())
    }

    @Test
    fun shouldFailGetUserByUserIdWithEntityNotFoundRestException() {
        // WHEN-THEN
        assertThat {
            userService.getUserByUserId(ObjectId().toString())
        }
            .isFailure()
            .isInstanceOf(RestException::class.java)
            .all {
                prop(RestException::statusCode).isEqualTo(HttpStatus.NOT_FOUND)
                prop(RestException::message).contains(USER_NOT_FOUND_ERROR_MESSAGE)
            }
    }

    companion object {
        private val userFieldsToIgnoring = listOf(User::createdDate, User::lastModifiedDate, User::version)
        private const val USER_ALREADY_EXISTS_ERROR_MESSAGE = "USER already exists. Details: "
        private const val USER_NOT_FOUND_ERROR_MESSAGE = "USER not found. Detail: "
        private const val DEPARTMENT_NOT_FOUND_ERROR_MESSAGE = "DEPARTMENT not found. Detail: "
    }
}
