package learning.diplom.user.svc.stub

import learning.diplom.department.svc.api.reqresp.user.svc.AddUserToDepartmentRequest
import learning.diplom.department.svc.api.reqresp.user.svc.AddUserToDepartmentResponse
import learning.diplom.model.error.lib.Error
import learning.diplom.model.error.lib.Success
import learning.diplom.user.svc.openfeign.client.DepartmentSvcClient
import org.mockito.BDDMockito.given
import org.slf4j.LoggerFactory
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.stereotype.Service

@Service
class DepartmentSvcClientStub {

    @MockBean
    private lateinit var departmentSvcClientMockBean: DepartmentSvcClient

    fun mockAddUserToDepartmentSuccessResponse(
        departmentId: String,
        addUserToDepartmentRequest: AddUserToDepartmentRequest
    ) {
        val successResponse = AddUserToDepartmentResponse.newBuilder().setSuccess(Success.getDefaultInstance()).build()

        mockAddUserToDepartment(departmentId, addUserToDepartmentRequest, successResponse)
    }

    fun mockAddUserToDepartmentErrorResponse(
        departmentId: String,
        addUserToDepartmentRequest: AddUserToDepartmentRequest,
        protoError: Error
    ) {
        val errorResponse = AddUserToDepartmentResponse.newBuilder()
            .setError(protoError)
            .build()

        mockAddUserToDepartment(departmentId, addUserToDepartmentRequest, errorResponse)
    }

    fun mockAddUserToDepartment(
        departmentId: String,
        addUserToDepartmentRequest: AddUserToDepartmentRequest,
        addUserToDepartmentResponse: AddUserToDepartmentResponse
    ) {
        LOG.info(
            "Mocking departmentId = {}, request = {}, response = {}",
            departmentId,
            addUserToDepartmentRequest,
            addUserToDepartmentResponse
        )
        given(departmentSvcClientMockBean.addUserToDepartment(departmentId, addUserToDepartmentRequest))
            .willReturn(addUserToDepartmentResponse)
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(DepartmentSvcClientStub::class.java)
    }

}
