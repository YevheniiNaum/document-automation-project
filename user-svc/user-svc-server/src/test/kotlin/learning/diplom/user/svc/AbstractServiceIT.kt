package learning.diplom.user.svc

import learning.diplom.user.svc.fixture.UserFixture.randomDbUser
import learning.diplom.user.svc.model.User
import learning.diplom.user.svc.repository.UserRepository
import learning.diplom.user.svc.service.UserService
import learning.diplom.user.svc.stub.DepartmentSvcClientStub
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.NONE,
    properties = [
        "scheduler.enabled=true"
    ]
)
@ActiveProfiles("test, dev")
abstract class AbstractServiceIT {

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var departmentSvcClientStub: DepartmentSvcClientStub

    fun createDbUser(): User {
        return userRepository.save(randomDbUser())
    }

    fun createDbUser(user: User): User {
        return userRepository.save(user)
    }
}
