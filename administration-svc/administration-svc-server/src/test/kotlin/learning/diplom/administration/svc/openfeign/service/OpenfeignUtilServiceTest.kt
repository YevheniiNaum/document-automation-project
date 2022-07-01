package learning.diplom.administration.svc.openfeign.service

import learning.diplom.administration.svc.AbstractServiceIT
import learning.diplom.administration.svc.api.reqresp.department.svc.AddDepartmentToAdministrationRequest
import learning.diplom.administration.svc.api.reqresp.department.svc.AddDepartmentToAdministrationResponse
import learning.diplom.administration.svc.fixture.AdministrationFixture
import learning.diplom.administration.svc.fixture.BriefDepartmentFixture.randomBriefDepartment
import learning.diplom.administration.svc.fixture.BriefDepartmentFixture.randomBriefDepartmentWithDepartmentType
import learning.diplom.administration.svc.fixture.EnumFixture
import learning.diplom.administration.svc.fixture.request.DepartmentSvcRequestFixture.randomAddDepartmentToAdministrationRequest
import learning.diplom.administration.svc.fixture.request.DepartmentSvcRequestFixture.randomAddDepartmentToAdministrationRequestWithDepartmentType
import learning.diplom.administration.svc.model.BriefDepartment
import learning.diplom.model.error.lib.department.svc.DepartmentType
import org.assertj.core.api.Assertions
import org.bson.types.ObjectId
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class OpenfeignUtilServiceTest : AbstractServiceIT() {

    @Test
    fun shouldReturnNullWhenAddDepartmentToAdministrationCheckPreconditions() {
        // GIVEN
        val administration = AdministrationFixture.randomAdministration().copy(
            departments = mutableListOf(
                randomBriefDepartmentWithDepartmentType(BriefDepartment.DepartmentType.FINANCIAL_DEPARTMENT)
            )
        )

        val request =
            randomAddDepartmentToAdministrationRequestWithDepartmentType(DepartmentType.ARCHITECTURE_DEPARTMENT)

        // WHEN
        val result = openfeignUtilService.addDepartmentToAdministrationCheckPreconditions(
            request,
            administration,
            AddDepartmentToAdministrationResponse.newBuilder()
        )

        // THEN
        Assertions.assertThat(result).isNull()
    }

    @Test
    fun shouldReturnNullWhenAddDepartmentToAdministrationCheckPreconditionsWithEmptyDepartmentsList() {
        // GIVEN
        val administration = AdministrationFixture.randomAdministration().copy(departments = mutableListOf())

        val request = randomAddDepartmentToAdministrationRequest()

        // WHEN
        val result = openfeignUtilService.addDepartmentToAdministrationCheckPreconditions(
            request,
            administration,
            AddDepartmentToAdministrationResponse.newBuilder()
        )

        // THEN
        Assertions.assertThat(result).isNull()
    }
}
