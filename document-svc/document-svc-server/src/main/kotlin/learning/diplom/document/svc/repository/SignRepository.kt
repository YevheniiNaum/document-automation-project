package learning.diplom.document.svc.repository

import learning.diplom.document.svc.model.Sign
import org.springframework.data.mongodb.repository.MongoRepository

interface SignRepository: MongoRepository<Sign, String> {

    fun findSignByDepartmentId(departmentId: String): Sign
    fun existsByDepartmentId(departmentId: String): Boolean
}
