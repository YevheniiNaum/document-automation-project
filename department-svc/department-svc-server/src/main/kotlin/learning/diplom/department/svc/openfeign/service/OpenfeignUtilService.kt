package learning.diplom.department.svc.openfeign.service

import learning.diplom.department.svc.api.reqresp.user.svc.AddUserToDepartmentRequest
import learning.diplom.department.svc.api.reqresp.user.svc.AddUserToDepartmentResponse
import learning.diplom.department.svc.model.Department
import learning.diplom.model.error.lib.Entity
import learning.diplom.model.error.lib.exception.proto.ProtoErrorUtil.createEntityAlreadyExistsErrorBuilder
import learning.diplom.model.error.lib.exception.proto.ProtoErrorUtil.createEntityLimitExceededErrorBuilder
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class OpenfeignUtilService {
    fun addUserToDepartmentCheckPreconditions(
        request: AddUserToDepartmentRequest,
        department: Department,
        responseBuilder: AddUserToDepartmentResponse.Builder
    ): AddUserToDepartmentResponse? {
        LOG.info("Check preconditions for adding user = {} to Department = {}", request, department)
        if (department.users.isEmpty()) {
            return null
        }

        val usersSizeWithNewUser = department.users.size + 1
        if (usersSizeWithNewUser >= department.usersLimit!!) {
            return responseBuilder
                .setError(
                    createEntityLimitExceededErrorBuilder(
                        Entity.USER,
                        "request = $request, department = $department"
                    )
                )
                .build()
        }

        if (department.users.map { it.userId }.contains(request.userId)) {
            return responseBuilder
                .setError(
                    createEntityAlreadyExistsErrorBuilder(
                        Entity.USER,
                        "request = $request, department = $department"
                    )
                )
                .build()
        }
        return null
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(OpenfeignUtilService::class.java)
    }
}
