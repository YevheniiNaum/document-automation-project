package learning.diplom.department.svc.model

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class BriefUser(
    @NotNull
    var userId: String? = null,
    @NotBlank
    var name: String? = null,
    @NotBlank
    var surname: String? = null,
    @NotNull
    var role: Role? = null
) {
    enum class Role {
        HEAD_OF_ADMINISTRATION,
        DEPUTY_HEAD_OF_ADMINISTRATION,
        HEAD_OF_DEPARTMENT,
        DEPUTY_HEAD_OF_DEPARTMENT,
        EMPLOYEE
    }
}
