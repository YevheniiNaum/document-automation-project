package learning.diplom.administration.svc.openfeign.controller.update

import learning.diplom.administration.svc.api.reqresp.department.svc.AddDepartmentToAdministrationRequest
import learning.diplom.administration.svc.api.reqresp.department.svc.AddDepartmentToAdministrationResponse
import learning.diplom.administration.svc.openfeign.service.OpenfeignAdministrationService
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(
    "/administration",
    consumes = ["application/x-protobuf"],
    produces = ["application/x-protobuf"]
)
class OpenfeignUpdateController(
    private val openfeignAdministrationService: OpenfeignAdministrationService
) {

    @PostMapping("/{administrationId}/add-department")
    fun addDepartmentToAdministration(
        @PathVariable administrationId: String,
        @RequestBody request: AddDepartmentToAdministrationRequest
    ): AddDepartmentToAdministrationResponse {
        LOG.info(
            "Add department to administration: administrationId = {}, administrationId = {}",
            administrationId,
            request.departmentId
        )
        return openfeignAdministrationService.addDepartmentToAdministration(administrationId, request)
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(OpenfeignUpdateController::class.java)
    }
}
