package learning.diplom.user.svc.fixture

import learning.diplom.user.svc.model.User
import org.apache.commons.lang.RandomStringUtils.randomAlphabetic
import org.bson.types.ObjectId
import org.jeasy.random.EasyRandom
import org.jeasy.random.EasyRandomParameters
import org.jeasy.random.FieldPredicates.named
import org.jeasy.random.api.Randomizer
import kotlin.random.Random

object UserFixture {

    private val DEFAULT_RANDOM_PARAMS: EasyRandomParameters
        get() = EasyRandomParameters()
            .seed(Random.nextLong())
            .collectionSizeRange(1, 2)
            .overrideDefaultInitialization(true)
            .randomize(named(User::userId.name), OBJECT_ID_RANDOMIZER)
            .randomize(named(User::name.name)) {
                randomAlphabetic(6)
            }
            .randomize(named(User::surname.name)) {
                randomAlphabetic(10)
            }
            .randomize(named(User::patronymic.name)) {
                randomAlphabetic(12)
            }
            .randomize(named(User::email.name), EMAIL_RANDOMIZER)
            .randomize(named(User::departmentId.name), OBJECT_ID_RANDOMIZER)

    private val OBJECT_ID_RANDOMIZER = Randomizer { ObjectId().toString() }
    private val EMAIL_RANDOMIZER = Randomizer { "${randomAlphabetic(6)}@${randomAlphabetic(5)}" }

    @get:Synchronized
    private val DB_EASY_RANDOM = EasyRandom(
        DEFAULT_RANDOM_PARAMS
            .excludeField(named(User::createdDate.name))
            .excludeField(named(User::lastModifiedDate.name))
            .excludeField(named(User::version.name))
    )

    @get:Synchronized
    private val MAP_EASY_RANDOM = EasyRandom(DEFAULT_RANDOM_PARAMS)

    fun randomDbUser(): User {
        return DB_EASY_RANDOM.nextObject(User::class.java).copy(deleted = false)
    }

    fun randomDbUserWithRole(role: User.Role): User {
        return DB_EASY_RANDOM.nextObject(User::class.java).copy(role = role, deleted = false)
    }

    fun randomUser(): User {
        return MAP_EASY_RANDOM.nextObject(User::class.java).copy(deleted = false)
    }

    fun randomUserWithRoles(role: User.Role): User {
        return MAP_EASY_RANDOM.nextObject(User::class.java).copy(role = role, deleted = false)
    }
}
