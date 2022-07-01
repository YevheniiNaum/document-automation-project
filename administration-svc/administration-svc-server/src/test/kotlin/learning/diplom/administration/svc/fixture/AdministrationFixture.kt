package learning.diplom.administration.svc.fixture

import learning.diplom.administration.svc.fixture.BriefDepartmentFixture.randomBriefDepartment
import learning.diplom.administration.svc.model.Administration
import learning.diplom.administration.svc.model.BriefDepartment
import org.apache.commons.lang.RandomStringUtils
import org.bson.types.ObjectId
import org.jeasy.random.EasyRandom
import org.jeasy.random.EasyRandomParameters
import org.jeasy.random.FieldPredicates.named
import org.jeasy.random.api.Randomizer
import java.time.Instant
import kotlin.random.Random
import kotlin.random.Random.Default.nextInt
import kotlin.random.Random.Default.nextLong

object AdministrationFixture {

    private val DEFAULT_RANDOM_PARAMS: EasyRandomParameters
        get() = EasyRandomParameters()
            .seed(nextLong())
            .collectionSizeRange(1, 2)
            .overrideDefaultInitialization(true)
            .randomize(named(Administration::administrationId.name), OBJECT_ID_RANDOMIZER)
            .randomize(named(Administration::headOfAdministrationId.name), OBJECT_ID_RANDOMIZER)
            .randomize(named(Administration::deputyHeadOfAdministrationId.name), OBJECT_ID_RANDOMIZER)

    private val OBJECT_ID_RANDOMIZER = Randomizer { ObjectId().toString() }

    private val BRIEF_DEPARTMENT_RANDOMIZER = Randomizer { randomBriefDepartment() }

    @get:Synchronized
    private val DB_EASY_RANDOM = EasyRandom(
        DEFAULT_RANDOM_PARAMS
            .excludeField(named(Administration::createdDate.name))
            .excludeField(named(Administration::lastModifiedDate.name))
            .excludeField(named(Administration::version.name))
    )

    @get:Synchronized
    private val MAPPING_EASY_RANDOM = EasyRandom(DEFAULT_RANDOM_PARAMS)

    fun randomDbAdministration(): Administration {
        return DB_EASY_RANDOM.nextObject(Administration::class.java).copy(deleted = false)
    }

    fun randomAdministration(): Administration {
        return MAPPING_EASY_RANDOM.nextObject(Administration::class.java)
            .copy(
                deleted = false,
                createdDate = Instant.now().minusSeconds(nextInt().toLong()),
                lastModifiedDate = Instant.now()
            )
    }
}
