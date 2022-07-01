package learning.diplom.document.svc.model

import org.bson.types.Binary
import org.springframework.data.annotation.*
import java.time.Instant
import javax.validation.constraints.NotNull

data class Sign(
    @Id
    @get:ReadOnlyProperty
    var signId: String? = null,
    @get:NotNull
    var departmentId: String? = null,
    var publicKey: Binary? = null,
    var privateKey: Binary? = null,
    @CreatedDate
    var createdDate: Instant? = null,
    @LastModifiedDate
    var lastModifiedDate: Instant? = null,
    @Version
    var version: Long? = null
) {
}
