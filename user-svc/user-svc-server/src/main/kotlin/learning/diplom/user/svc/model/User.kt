package learning.diplom.user.svc.model

import com.fasterxml.jackson.annotation.JsonIgnore
import learning.diplom.model.error.lib.department.svc.DepartmentType
import org.jetbrains.annotations.NotNull
import org.springframework.data.annotation.*
import java.time.Instant
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class User(
    @Id
    @get:ReadOnlyProperty
    var userId: String? = null,
    @NotBlank
    @get:Size(min = 3, max = 16)
    var name: String? = null,
    @NotBlank
    @get:Size(min = 4, max = 20)
    var surname: String? = null,
    @NotBlank
    @get:Size(min = 4, max = 20)
    var patronymic: String? = null,
    @NotNull
    @get:Email
    @get:Size(max = 100)
    var email: String? = null,
    @get:NotNull
    var role: Role? = null,
    @NotNull
    @get:JsonIgnore
    var passwordHash: String? = null,
    @NotNull
    var departmentId: String? = null,
    @CreatedDate
    var createdDate: Instant? = null,
    @LastModifiedDate
    var lastModifiedDate: Instant? = null,
    var deleted: Boolean? = false,
    @Version
    var version: Long? = null,
) {
    enum class Role {
        HEAD_OF_ADMINISTRATION,
        DEPUTY_HEAD_OF_ADMINISTRATION,
        HEAD_OF_DEPARTMENT,
        DEPUTY_HEAD_OF_DEPARTMENT,
        EMPLOYEE
    }
}
