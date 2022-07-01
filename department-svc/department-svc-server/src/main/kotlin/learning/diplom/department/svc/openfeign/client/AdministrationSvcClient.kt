package learning.diplom.department.svc.openfeign.client

import learning.diplom.administration.svc.api.reqresp.department.svc.AddDepartmentToAdministrationRequest
import learning.diplom.administration.svc.api.reqresp.department.svc.AddDepartmentToAdministrationResponse
import learning.diplom.department.svc.config.OpenFeignConfiguration
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignClient("\${administration-svc-feign-client}", configuration = [OpenFeignConfiguration::class])
interface AdministrationSvcClient {

    @PostMapping(
        "/administration/{administrationId}/add-department",
        consumes = ["application/x-protobuf"],
        produces = ["application/x-protobuf"]
    )
    fun addDepartmentToAdministration(
        @PathVariable administrationId: String,
        @RequestBody request: AddDepartmentToAdministrationRequest
    ): AddDepartmentToAdministrationResponse
}
