package learning.diplom.department.svc

import learning.diplom.department.svc.repository.DepartmentRepository
import learning.diplom.department.svc.openfeign.service.OpenfeignDepartmentService
import learning.diplom.department.svc.openfeign.service.OpenfeignUtilService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.NONE,
    properties = [
        "scheduler.enabled=true"
    ]
)
@ActiveProfiles("test, dev")
abstract class AbstractServiceIT {

    @Autowired
    lateinit var departmentRepository: DepartmentRepository

    @Autowired
    lateinit var openfeignDepartmentService: OpenfeignDepartmentService

    @Autowired
    lateinit var openfeignUtilService: OpenfeignUtilService

//    @Autowired
//    lateinit var departmentSvcClientStub: DepartmentSvcClientStub
}
