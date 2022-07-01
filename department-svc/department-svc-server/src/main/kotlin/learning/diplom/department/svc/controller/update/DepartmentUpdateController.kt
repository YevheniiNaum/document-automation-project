package learning.diplom.department.svc.controller.update

import learning.diplom.department.svc.model.Department
import learning.diplom.department.svc.openfeign.service.OpenfeignDepartmentService
import learning.diplom.department.svc.service.DepartmentService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/department")
class DepartmentUpdateController(
    private val departmentService: DepartmentService
) {

    @PostMapping("/")
    fun createDepartment(@Valid @RequestBody department: Department): ResponseEntity<Department> {
        LOG.info("Create department = {}", department)
        return ResponseEntity(departmentService.createDepartment(department), HttpStatus.CREATED)

    }

    companion object {
        private val LOG = LoggerFactory.getLogger(DepartmentUpdateController::class.java)
    }
}
