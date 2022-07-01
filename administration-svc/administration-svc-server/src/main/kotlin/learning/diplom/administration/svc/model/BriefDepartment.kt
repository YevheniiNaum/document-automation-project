package learning.diplom.administration.svc.model

import javax.validation.constraints.NotNull

data class BriefDepartment(
    @NotNull
    var departmentId: String? = null,
    @NotNull
    var departmentType: DepartmentType? = null,
    @NotNull
    var headOfDepartmentId: String? = null
) {
    enum class DepartmentType {
        GENERAL_DEPARTMENT,
        FINANCIAL_DEPARTMENT,
        HUMAN_RESOURCES_DEPARTMENT,
        ARCHITECTURE_DEPARTMENT
    }
}
