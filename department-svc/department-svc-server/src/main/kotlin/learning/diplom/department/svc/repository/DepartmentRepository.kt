package learning.diplom.department.svc.repository

import learning.diplom.department.svc.model.Department
import org.springframework.data.mongodb.repository.MongoRepository

interface DepartmentRepository : MongoRepository<Department, String> {
    fun findByDepartmentId(departmentId: String): Department
    fun deleteByDepartmentId(departmentId: String)
    fun existsByDepartmentId(departmentId: String): Boolean
}
