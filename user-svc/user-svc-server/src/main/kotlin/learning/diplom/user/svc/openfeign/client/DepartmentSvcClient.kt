package learning.diplom.user.svc.openfeign.client

import learning.diplom.department.svc.api.reqresp.user.svc.AddUserToDepartmentRequest
import learning.diplom.department.svc.api.reqresp.user.svc.AddUserToDepartmentResponse
import learning.diplom.user.svc.config.OpenFeignConfiguration
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import javax.ws.rs.core.MediaType

@FeignClient("\${department-svc-feign-client}", configuration = [OpenFeignConfiguration::class])
interface DepartmentSvcClient {

    @PostMapping(
        "department/{departmentId}/add-user",
        consumes = ["application/x-protobuf"],
        produces = ["application/x-protobuf"]
    )
    fun addUserToDepartment(
        @PathVariable departmentId: String,
        @RequestBody request: AddUserToDepartmentRequest
    ): AddUserToDepartmentResponse
}
