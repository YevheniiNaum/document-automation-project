package learning.diplom.department.svc.service

import learning.diplom.administration.svc.api.reqresp.department.svc.AddDepartmentToAdministrationRequest
import learning.diplom.department.svc.model.Department
import learning.diplom.department.svc.openfeign.client.AdministrationSvcClient
import learning.diplom.department.svc.repository.DepartmentRepository
import learning.diplom.department.svc.util.codec.asEnumOf
import learning.diplom.model.error.lib.ServerEntity
import learning.diplom.model.error.lib.exception.rest.EntityAlreadyExistsRestException
import learning.diplom.model.error.lib.exception.rest.RestException
import learning.diplom.model.error.lib.mapper.ErrorMapper.mapToRestException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
class DepartmentService(
    private val departmentRepository: DepartmentRepository,
    private val administrationSvcClient: AdministrationSvcClient
) {
    //// UPDATE METHODS ////
    fun createDepartment(department: Department): Department {
        if (departmentRepository.existsByDepartmentId(department.departmentId!!)) {
            throw EntityAlreadyExistsRestException(ServerEntity.USER, "department = $department")
        }

        val savedDepartment = departmentRepository.save(department)
        LOG.info("Started adding department = {}, to administration = {}", savedDepartment.departmentId)

        val addDepartmentToAdministrationResponse = try {
            sendAddDepartmentToAdministrationRequest(department)
        } catch (ex: Exception) {
            LOG.error("ERROR from administration-svc: ", ex)
            departmentRepository.deleteByDepartmentId(savedDepartment.departmentId!!)
            throw RestException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal error")
        }

        if (addDepartmentToAdministrationResponse.hasError()) {
            departmentRepository.deleteByDepartmentId(savedDepartment.departmentId!!)
            throw addDepartmentToAdministrationResponse.error.mapToRestException()
        }
        return savedDepartment
    }

    //// READ METHODS ////

    private fun sendAddDepartmentToAdministrationRequest(department: Department) =
        administrationSvcClient.addDepartmentToAdministration(
            department.administrationId!!,
            AddDepartmentToAdministrationRequest.newBuilder()
                .setDepartmentId(department.departmentId)
                .setHeadOfDepartmentId(department.headOfDepartmentId)
                .setDepartmentType(department.departmentType.asEnumOf())
                .build()
        )

    companion object {
        private val LOG = LoggerFactory.getLogger(DepartmentService::class.java)
    }
}
