package learning.diplom.department.svc.openfeign.controller.update

import learning.diplom.department.svc.api.reqresp.user.svc.AddUserToDepartmentRequest
import learning.diplom.department.svc.api.reqresp.user.svc.AddUserToDepartmentResponse
import learning.diplom.department.svc.openfeign.service.OpenfeignDepartmentService
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(
    "/department",
    consumes = ["application/x-protobuf"],
    produces = ["application/x-protobuf"]
)
class OpenfeignUpdateController(
    private val openfeignDepartmentService: OpenfeignDepartmentService
) {

    @PostMapping("/{departmentId}/add-user")
    fun addUserToDepartment(
        @PathVariable departmentId: String,
        @RequestBody request: AddUserToDepartmentRequest
    ): AddUserToDepartmentResponse {
        LOG.info("Add user to department: departmentId = {}, userId = {}", departmentId, request.userId)
        return openfeignDepartmentService.addUserToDepartment(departmentId, request)
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(OpenfeignUpdateController::class.java)
    }
}
