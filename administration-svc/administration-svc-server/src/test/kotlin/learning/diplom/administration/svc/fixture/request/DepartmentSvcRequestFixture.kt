package learning.diplom.administration.svc.fixture.request

import learning.diplom.administration.svc.api.reqresp.department.svc.AddDepartmentToAdministrationRequest
import learning.diplom.administration.svc.fixture.EnumFixture
import learning.diplom.model.error.lib.department.svc.DepartmentType
import org.bson.types.ObjectId

object DepartmentSvcRequestFixture {

    fun randomAddDepartmentToAdministrationRequest (): AddDepartmentToAdministrationRequest{
        return AddDepartmentToAdministrationRequest.newBuilder()
            .setDepartmentId(ObjectId().toString())
            .setDepartmentType(EnumFixture.randomProtoRecognizedEnum(DepartmentType::class.java))
            .setHeadOfDepartmentId(ObjectId().toString())
            .build()
    }

    fun randomAddDepartmentToAdministrationRequestWithDepartmentType (departmentType: DepartmentType): AddDepartmentToAdministrationRequest{
        return AddDepartmentToAdministrationRequest.newBuilder()
            .setDepartmentId(ObjectId().toString())
            .setDepartmentType(departmentType)
            .setHeadOfDepartmentId(ObjectId().toString())
            .build()
    }
}
