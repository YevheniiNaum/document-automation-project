package learning.diplom.administration.svc.openfeign.service

import learning.diplom.administration.svc.api.reqresp.department.svc.AddDepartmentToAdministrationRequest
import learning.diplom.administration.svc.api.reqresp.department.svc.AddDepartmentToAdministrationResponse
import learning.diplom.administration.svc.model.Administration
import learning.diplom.administration.svc.model.BriefDepartment
import learning.diplom.administration.svc.util.codec.asEnumOf
import learning.diplom.model.error.lib.Entity
import learning.diplom.model.error.lib.exception.proto.ProtoErrorUtil.createEntityAlreadyExistsErrorBuilder
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class OpenfeignUtilService {
    fun addDepartmentToAdministrationCheckPreconditions(
        request: AddDepartmentToAdministrationRequest,
        administration: Administration?,
        responseBuilder: AddDepartmentToAdministrationResponse.Builder
    ): AddDepartmentToAdministrationResponse? {
        LOG.info(
            "Check preconditions for adding department = {} to administration = {}",
            request,
            administration
        )

        if (administration!!.departments.isEmpty()) {
            return null
        }

        if (administration.departments.map { it.departmentType }
                .contains(request.departmentType.asEnumOf<BriefDepartment.DepartmentType>())) {
            return responseBuilder
                .setError(
                    createEntityAlreadyExistsErrorBuilder(
                        Entity.DEPARTMENT,
                        "department = $request, administration = $administration"
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
