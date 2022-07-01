package learning.diplom.administration.svc.repository

import learning.diplom.administration.svc.model.Administration
import org.springframework.data.mongodb.repository.MongoRepository

interface AdministrationRepository : MongoRepository<Administration, String> {
    fun findByAdministrationId(administrationId: String): Administration
}
