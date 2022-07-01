package learning.diplom.department.svc.openfeign.service

import learning.diplom.department.svc.AbstractServiceIT
import learning.diplom.department.svc.api.reqresp.user.svc.AddUserToDepartmentResponse
import learning.diplom.department.svc.fixture.BriefUserFixture.randomBriefUser
import learning.diplom.department.svc.fixture.DepartmentFixture.randomDbDepartment
import learning.diplom.department.svc.fixture.DepartmentFixture.randomDepartment
import learning.diplom.department.svc.fixture.request.UserSvcRequestFixture.randomAddUserToDepartmentRequest
import learning.diplom.model.error.lib.Entity
import learning.diplom.model.error.lib.exception.proto.ProtoErrorUtil.createEntityAlreadyExistsErrorBuilder
import learning.diplom.model.error.lib.exception.proto.ProtoErrorUtil.createEntityLimitExceededErrorBuilder
import org.assertj.core.api.Assertions.assertThat
import org.bson.types.ObjectId
import org.junit.jupiter.api.Test

internal class OpenfeignUtilServiceTest : AbstractServiceIT() {

    @Test
    fun shouldReturnNullForAddUserToDepartmentCheckPreconditionsWhenUsersListIsEmpty() {
        // GIVEN
        val department = randomDepartment().copy(users = mutableListOf())
        val request = randomAddUserToDepartmentRequest()

        // WHEN
        val result = openfeignUtilService.addUserToDepartmentCheckPreconditions(
            request,
            department,
            AddUserToDepartmentResponse.newBuilder()
        )

        // THEN
        assertThat(result).isNull()
    }

    @Test
    fun test() {
        departmentRepository.save(randomDbDepartment().copy(departmentId = ObjectId().toString()))
    }

    @Test
    fun shouldReturnNullForAddUserToDepartmentCheckPreconditions() {
        // GIVEN
        val department = randomDepartment().copy(usersLimit = 10)
        val request = randomAddUserToDepartmentRequest()

        // WHEN
        val result = openfeignUtilService.addUserToDepartmentCheckPreconditions(
            request,
            department,
            AddUserToDepartmentResponse.newBuilder()
        )

        // THEN
        assertThat(result).isNull()
    }

    @Test
    fun shouldReturnEntityLimitErrorForAddUserToDepartmentCheckPreconditions() {
        // GIVEN
        val department = randomDepartment().copy(usersLimit = 1)
        val request = randomAddUserToDepartmentRequest()

        val expectedErrorResponse = AddUserToDepartmentResponse.newBuilder()
            .setError(
                createEntityLimitExceededErrorBuilder(
                    Entity.USER,
                    "request = $request, department = $department"
                )
            )
            .build()

        // WHEN
        val result = openfeignUtilService.addUserToDepartmentCheckPreconditions(
            request,
            department,
            AddUserToDepartmentResponse.newBuilder()
        )

        // THEN
        assertThat(result).isEqualTo(expectedErrorResponse)
    }

    @Test
    fun shouldReturnUserAlreadyExistsErrorForAddUserToDepartmentCheckPreconditions() {
        // GIVEN
        val request = randomAddUserToDepartmentRequest()
        val department = randomDepartment().copy(
            usersLimit = 10,
            users = mutableListOf(randomBriefUser().copy(userId = request.userId))
        )

        val expectedErrorResponse = AddUserToDepartmentResponse.newBuilder()
            .setError(
                createEntityAlreadyExistsErrorBuilder(
                    Entity.USER,
                    "request = $request, department = $department"
                )
            )
            .build()

        // WHEN
        val result = openfeignUtilService.addUserToDepartmentCheckPreconditions(
            request,
            department,
            AddUserToDepartmentResponse.newBuilder()
        )

        // THEN
        assertThat(result).isEqualTo(expectedErrorResponse)
    }
}
