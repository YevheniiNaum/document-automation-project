package learning.diplom.administration.svc.model

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import java.time.Instant
import javax.validation.constraints.NotNull

data class Administration(
    @Id
    var administrationId: String? = null,
    @get:NotNull
    var name: String? = null,
    var headOfAdministrationId: String? = null,
    var deputyHeadOfAdministrationId: String? = null,
    var departments: MutableList<BriefDepartment> = mutableListOf(),
    @CreatedDate
    var createdDate: Instant? = null,
    @LastModifiedDate
    var lastModifiedDate: Instant? = null,
    @Version
    val version: Long? = null,
    var deleted: Boolean? = false
)
