package learning.diplom.department.svc.openfeign.service

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import learning.diplom.department.svc.util.codec.mapAsEnumOf
import learning.diplom.department.svc.api.reqresp.user.svc.AddUserToDepartmentRequest
import learning.diplom.department.svc.api.reqresp.user.svc.AddUserToDepartmentResponse
import learning.diplom.department.svc.model.BriefUser
import learning.diplom.department.svc.repository.DepartmentRepository
import learning.diplom.department.svc.util.codec.asEnumOf
import learning.diplom.model.error.lib.Entity
import learning.diplom.model.error.lib.Success
import learning.diplom.model.error.lib.exception.proto.ProtoErrorUtil
import org.slf4j.LoggerFactory
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.stereotype.Service

@Service
class OpenfeignDepartmentService(
    private val departmentRepository: DepartmentRepository,
    private val openfeignUtilService: OpenfeignUtilService
) {
    fun addUserToDepartment(departmentId: String, request: AddUserToDepartmentRequest): AddUserToDepartmentResponse {
        val responseBuilder = AddUserToDepartmentResponse.newBuilder()

        val department = try {
            departmentRepository.findByDepartmentId(departmentId)
        } catch (ex: EmptyResultDataAccessException) {
            return responseBuilder
                .setError(ProtoErrorUtil.createEntityNotFoundErrorBuilder(
                    Entity.DEPARTMENT,
                    "departmentId = $departmentId")
                )
                .build()
        }

        openfeignUtilService.addUserToDepartmentCheckPreconditions(request, department, responseBuilder)
            ?.let { return it }

        if (department.headOfDepartmentId == null) {
            department.headOfDepartmentId = request.userId
        }
        department.users.add(
            BriefUser(
                request.userId,
                request.name,
                request.surname,
                request.role.asEnumOf<BriefUser.Role>()
            )
        )
        departmentRepository.save(department)

        LOG.info("Adding user = {} was successful", request)

        return responseBuilder
            .setSuccess(Success.getDefaultInstance())
            .build()
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(OpenfeignDepartmentService::class.java)
    }
}
