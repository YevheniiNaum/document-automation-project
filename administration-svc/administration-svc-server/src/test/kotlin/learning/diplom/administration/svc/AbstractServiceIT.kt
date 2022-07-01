package learning.diplom.administration.svc

import learning.diplom.administration.svc.fixture.AdministrationFixture.randomDbAdministration
import learning.diplom.administration.svc.model.Administration
import learning.diplom.administration.svc.openfeign.service.OpenfeignAdministrationService
import learning.diplom.administration.svc.openfeign.service.OpenfeignUtilService
import learning.diplom.administration.svc.repository.AdministrationRepository
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
    lateinit var administrationRepository: AdministrationRepository

    @Autowired
    lateinit var openfeignAdministrationService: OpenfeignAdministrationService

    @Autowired
    lateinit var openfeignUtilService: OpenfeignUtilService

    fun createDbAdministration(): Administration {
        return administrationRepository.save(randomDbAdministration())
    }

    fun createDbAdministration(administration: Administration): Administration {
        return administrationRepository.save(administration)
    }
}
