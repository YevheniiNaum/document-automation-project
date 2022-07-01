package learning.diplom.department.svc.fixture.request

import learning.diplom.department.svc.api.reqresp.user.svc.AddUserToDepartmentRequest
import learning.diplom.department.svc.fixture.EnumFixture.randomProtoRecognizedEnum
import learning.diplom.department.svc.fixture.EnumFixture.randomProtoRecognizedEnums
import learning.diplom.model.error.lib.user.svc.Role
import org.apache.commons.lang.RandomStringUtils.randomAlphabetic
import org.bson.types.ObjectId
import kotlin.random.Random.Default.nextInt

object UserSvcRequestFixture {

    fun randomAddUserToDepartmentRequest(): AddUserToDepartmentRequest {
        return AddUserToDepartmentRequest.newBuilder()
            .setUserId(ObjectId().toString())
            .setName(randomAlphabetic(7))
            .setSurname(randomAlphabetic(10))
            .setRole(randomProtoRecognizedEnum(Role::class.java))
            .build()
    }

    fun randomAddUserToDepartmentRequestWithRoles(role: Role): AddUserToDepartmentRequest {
        return AddUserToDepartmentRequest.newBuilder()
            .setUserId(ObjectId().toString())
            .setName(randomAlphabetic(7))
            .setSurname(randomAlphabetic(10))
            .setRole(role)
            .build()
    }
}
