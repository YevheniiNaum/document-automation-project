package learning.diplom.department.svc.fixture

import learning.diplom.department.svc.model.BriefUser
import org.apache.commons.lang.RandomStringUtils.randomAlphabetic
import org.bson.types.ObjectId
import org.jeasy.random.EasyRandom
import org.jeasy.random.EasyRandomParameters
import org.jeasy.random.FieldPredicates.named
import org.jeasy.random.api.Randomizer
import kotlin.random.Random

object BriefUserFixture {

    private val DEFAULT_RANDOM_PARAMS: EasyRandomParameters
        get() = EasyRandomParameters()
            .seed(Random.nextLong())
            .collectionSizeRange(1, 2)
            .overrideDefaultInitialization(true)
            .randomize(named(BriefUser::userId.name), OBJECT_ID_RANDOMIZER)
            .randomize(named(BriefUser::name.name)) {
                randomAlphabetic(7)
            }
            .randomize(named(BriefUser::surname.name)) {
                randomAlphabetic(10)
            }
            .randomize(named(BriefUser::role.name)) {
                EnumFixture.randomEnum(BriefUser.Role::class.java)
            }

    private val OBJECT_ID_RANDOMIZER = Randomizer { ObjectId().toString() }

    @get:Synchronized
    private val MAP_EASY_RANDOM = EasyRandom(DEFAULT_RANDOM_PARAMS)

    fun randomBriefUser(): BriefUser {
        return MAP_EASY_RANDOM.nextObject(BriefUser::class.java)
    }

    fun randomBriefUserWithRoles(role: BriefUser.Role?): BriefUser {
        return MAP_EASY_RANDOM.nextObject(BriefUser::class.java).copy(role = role)
    }
}
