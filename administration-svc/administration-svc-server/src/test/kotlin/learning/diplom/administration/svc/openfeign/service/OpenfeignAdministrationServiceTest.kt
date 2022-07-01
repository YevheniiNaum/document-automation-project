package learning.diplom.administration.svc.openfeign.service

import learning.diplom.administration.svc.AbstractServiceIT
import learning.diplom.administration.svc.api.reqresp.department.svc.AddDepartmentToAdministrationRequest
import learning.diplom.administration.svc.api.reqresp.department.svc.AddDepartmentToAdministrationResponse
import learning.diplom.administration.svc.fixture.AdministrationFixture.randomAdministration
import learning.diplom.administration.svc.fixture.AdministrationFixture.randomDbAdministration
import learning.diplom.administration.svc.fixture.EnumFixture.randomProtoRecognizedEnum
import learning.diplom.administration.svc.fixture.request.DepartmentSvcRequestFixture.randomAddDepartmentToAdministrationRequest
import learning.diplom.administration.svc.model.BriefDepartment
import learning.diplom.administration.svc.util.codec.asEnumOf
import learning.diplom.model.error.lib.Entity
import learning.diplom.model.error.lib.Success
import learning.diplom.model.error.lib.department.svc.DepartmentType
import learning.diplom.model.error.lib.exception.proto.ProtoErrorUtil.createEntityNotFoundErrorBuilder
import org.assertj.core.api.Assertions.assertThat
import org.bson.types.ObjectId
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Test

internal class OpenfeignAdministrationServiceTest : AbstractServiceIT() {

    @Test
    fun shouldAddDepartmentToAdministration() {
        // GIVEN
        val dbAdministration = createDbAdministration(randomDbAdministration().copy(departments = mutableListOf()))

        val request = randomAddDepartmentToAdministrationRequest()

        dbAdministration.departments.add(
            BriefDepartment(
                request.departmentId,
                request.departmentType.asEnumOf<BriefDepartment.DepartmentType>(),
                request.headOfDepartmentId
            )
        )
        // WHEN
        val result = openfeignAdministrationService.addDepartmentToAdministration(
            dbAdministration.administrationId!!,
            request
        )
        val updatedAdministration = administrationRepository.findByAdministrationId(dbAdministration.administrationId!!)

        // THEN
        assertThat(result).isEqualTo(
            AddDepartmentToAdministrationResponse.newBuilder()
                .setSuccess(Success.getDefaultInstance())
                .build()
        )
        assertThat(updatedAdministration).usingRecursiveComparison()
            .ignoringFields(*administrationFieldsToIgnoring.toTypedArray())
            .isEqualTo(dbAdministration)
    }

    @Test
    fun shouldFailAddDepartmentToAdministrationWithEntityNotFound() {
        // GIVEN
        val randomAdministrationId = ObjectId().toString()
        val request = randomAddDepartmentToAdministrationRequest()

        // WHEN
        val result = openfeignAdministrationService.addDepartmentToAdministration(
            randomAdministrationId,
            request
        )

        // THEN
        assertThat(result).isEqualTo(
            AddDepartmentToAdministrationResponse.newBuilder().setError(
                createEntityNotFoundErrorBuilder(
                    Entity.ADMINISTRATION,
                    message = "administrationId = $randomAdministrationId"
                )
            )
                .build()
        )
    }

    companion object {
        private val administrationFieldsToIgnoring = listOf("createdDate", "lastModifiedDate", "version")
    }
}
