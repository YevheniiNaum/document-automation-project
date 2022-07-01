package learning.diplom.administration.svc.openfeign.service

import learning.diplom.administration.svc.api.reqresp.department.svc.AddDepartmentToAdministrationRequest
import learning.diplom.administration.svc.api.reqresp.department.svc.AddDepartmentToAdministrationResponse
import learning.diplom.administration.svc.model.BriefDepartment
import learning.diplom.administration.svc.repository.AdministrationRepository
import learning.diplom.administration.svc.util.codec.asEnumOf
import learning.diplom.model.error.lib.Entity
import learning.diplom.model.error.lib.Success
import learning.diplom.model.error.lib.exception.proto.ProtoErrorUtil.createEntityNotFoundErrorBuilder
import org.slf4j.LoggerFactory
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.stereotype.Service

@Service
class OpenfeignAdministrationService(
    private val administrationRepository: AdministrationRepository,
    private val openfeignUtilService: OpenfeignUtilService
) {
    fun addDepartmentToAdministration(
        administrationId: String,
        request: AddDepartmentToAdministrationRequest
    ): AddDepartmentToAdministrationResponse {
        val responseBuilder = AddDepartmentToAdministrationResponse.newBuilder()

        val administration = try {
            administrationRepository.findByAdministrationId(administrationId)
        } catch (ex: EmptyResultDataAccessException) {
            return responseBuilder
                .setError(
                    createEntityNotFoundErrorBuilder(
                        Entity.ADMINISTRATION,
                        "administrationId = $administrationId"
                    )
                )
                .build()
        }

        openfeignUtilService.addDepartmentToAdministrationCheckPreconditions(request, administration, responseBuilder)

        administration.departments.add(
            BriefDepartment(
                request.departmentId,
                request.departmentType.asEnumOf<BriefDepartment.DepartmentType>(),
                request.headOfDepartmentId
            )
        )

        administrationRepository.save(administration)
        LOG.info("Adding department with request = {} was successful", request)

        return responseBuilder
            .setSuccess(Success.getDefaultInstance())
            .build()
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(OpenfeignAdministrationService::class.java)
    }
}
