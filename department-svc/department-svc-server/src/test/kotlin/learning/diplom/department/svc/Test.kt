package learning.diplom.department.svc

import learning.diplom.department.svc.fixture.DepartmentFixture.randomDbDepartment
import org.junit.jupiter.api.Test

class Test: AbstractServiceIT() {

    @Test
    fun test() {
        departmentRepository.insert(randomDbDepartment())
    }
}
