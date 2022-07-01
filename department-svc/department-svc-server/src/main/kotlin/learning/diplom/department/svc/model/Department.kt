package learning.diplom.department.svc.model

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import java.time.Instant
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class Department(
    @Id
    var departmentId: String? = null,
    @get:NotNull
    var administrationId: String? = null,
    @get:NotBlank
    var name: String? = null,
    var headOfDepartmentId: String? = null,
    var users: MutableList<BriefUser> = mutableListOf(),
    @get:NotNull
    var usersLimit: Int? = null,
    @get:NotNull
    var departmentType: DepartmentType? = null,
    @CreatedDate
    var createdDate: Instant? = null,
    @LastModifiedDate
    var lastModifiedDate: Instant? = null,
    @Version
    val version: Long? = null,
    var deleted: Boolean? = false
) {
    enum class DepartmentType {
        GENERAL_DEPARTMENT,
        FINANCIAL_DEPARTMENT,
        HUMAN_RESOURCES_DEPARTMENT,
        ARCHITECTURE_DEPARTMENT
    }
}
