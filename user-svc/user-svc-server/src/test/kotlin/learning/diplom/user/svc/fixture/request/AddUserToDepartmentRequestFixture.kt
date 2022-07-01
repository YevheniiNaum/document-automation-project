package learning.diplom.user.svc.fixture.request

import learning.diplom.department.svc.api.reqresp.user.svc.AddUserToDepartmentRequest
import learning.diplom.model.error.lib.user.svc.Role
import learning.diplom.user.svc.fixture.EnumFixture.randomProtoRecognizedEnum
import learning.diplom.user.svc.fixture.EnumFixture.randomProtoRecognizedEnums
import learning.diplom.user.svc.model.User
import learning.diplom.user.svc.util.codec.asEnumOf
import learning.diplom.user.svc.util.codec.mapAsEnumOf
import org.apache.commons.lang.RandomStringUtils.randomAlphabetic
import org.bson.types.ObjectId
import kotlin.random.Random.Default.nextInt

object AddUserToDepartmentRequestFixture {

    fun randomAddUserToDepartmentRequest(): AddUserToDepartmentRequest {
        return AddUserToDepartmentRequest.newBuilder()
            .setUserId(ObjectId().toString())
            .setName(randomAlphabetic(6))
            .setSurname(randomAlphabetic(10))
            .setRole(randomProtoRecognizedEnum(Role::class.java))
            .build()
    }

    fun addUserToDepartmentRequest(user: User): AddUserToDepartmentRequest {
        return AddUserToDepartmentRequest.newBuilder()
            .setUserId(user.userId)
            .setName(user.name)
            .setSurname(user.surname)
            .setRole(user.role.asEnumOf())
            .build()
    }
}
