package learning.diplom.document.svc.model

import learning.diplom.model.error.lib.department.svc.DepartmentType
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import java.time.Instant
import javax.validation.constraints.NotBlank

data class Document(
    @Id
    var documentId: String? = null,
    @get:NotBlank
    var userId: String? = null,
    var fileName: String? = null,
    var contentType: String? = null,
    var size: Long? = null,
    var departmentId: String,
    // Approve fields
    var headOfAdministrationApprove: Boolean = false,
    var deputyHeadOfAdministrationApprove: Boolean = false,
    var headOfDepartmentApprove: Boolean = false,
    var deputyHeadOfDepartmentApprove: Boolean = false,
    var employeeApprove: Boolean = false,
    // SIGNATURE
    var signatureExists: Boolean = false,
    var signature: ByteArray = byteArrayOf(),
    var publicKey: ByteArray = byteArrayOf(),

    var departmentType: DepartmentType? = null,
    @CreatedDate
    var createdDate: Instant? = null,
    @LastModifiedDate
    var lastModifiedDate: Instant? = null,
    @Version
    val version: Long? = 0,
    var deleted: Boolean? = false
) {
    enum class DepartmentType {
        GENERAL_DEPARTMENT,
        FINANCIAL_DEPARTMENT,
        HUMAN_RESOURCES_DEPARTMENT,
        ARCHITECTURE_DEPARTMENT
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Document

        if (documentId != other.documentId) return false
        if (userId != other.userId) return false
        if (fileName != other.fileName) return false
        if (contentType != other.contentType) return false
        if (size != other.size) return false
        if (departmentId != other.departmentId) return false
        if (headOfAdministrationApprove != other.headOfAdministrationApprove) return false
        if (deputyHeadOfAdministrationApprove != other.deputyHeadOfAdministrationApprove) return false
        if (headOfDepartmentApprove != other.headOfDepartmentApprove) return false
        if (deputyHeadOfDepartmentApprove != other.deputyHeadOfDepartmentApprove) return false
        if (employeeApprove != other.employeeApprove) return false
        if (signatureExists != other.signatureExists) return false
        if (signature != null) {
            if (other.signature == null) return false
            if (!signature.contentEquals(other.signature)) return false
        } else if (other.signature != null) return false
        if (departmentType != other.departmentType) return false
        if (createdDate != other.createdDate) return false
        if (lastModifiedDate != other.lastModifiedDate) return false
        if (version != other.version) return false
        if (deleted != other.deleted) return false

        return true
    }

    override fun hashCode(): Int {
        var result = documentId?.hashCode() ?: 0
        result = 31 * result + (userId?.hashCode() ?: 0)
        result = 31 * result + (fileName?.hashCode() ?: 0)
        result = 31 * result + (contentType?.hashCode() ?: 0)
        result = 31 * result + (size?.hashCode() ?: 0)
        result = 31 * result + departmentId.hashCode()
        result = 31 * result + headOfAdministrationApprove.hashCode()
        result = 31 * result + deputyHeadOfAdministrationApprove.hashCode()
        result = 31 * result + headOfDepartmentApprove.hashCode()
        result = 31 * result + deputyHeadOfDepartmentApprove.hashCode()
        result = 31 * result + employeeApprove.hashCode()
        result = 31 * result + signatureExists.hashCode()
        result = 31 * result + (signature?.contentHashCode() ?: 0)
        result = 31 * result + (departmentType?.hashCode() ?: 0)
        result = 31 * result + (createdDate?.hashCode() ?: 0)
        result = 31 * result + (lastModifiedDate?.hashCode() ?: 0)
        result = 31 * result + (version?.hashCode() ?: 0)
        result = 31 * result + (deleted?.hashCode() ?: 0)
        return result
    }
}
