package learning.diplom.administration.svc.fixture

import learning.diplom.administration.svc.fixture.EnumFixture.randomEnum
import learning.diplom.administration.svc.model.Administration
import learning.diplom.administration.svc.model.BriefDepartment
import org.bson.types.ObjectId
import org.jeasy.random.EasyRandom
import org.jeasy.random.EasyRandomParameters
import org.jeasy.random.FieldPredicates.named
import org.jeasy.random.api.Randomizer
import kotlin.random.Random

object BriefDepartmentFixture {

    private val DEFAULT_RANDOM_PARAMS: EasyRandomParameters
        get() = EasyRandomParameters()
            .seed(Random.nextLong())
            .collectionSizeRange(1, 2)
            .overrideDefaultInitialization(true)
            .randomize(named(BriefDepartment::departmentId.name), OBJECT_ID_RANDOMIZER)
            .randomize(named(BriefDepartment::departmentType.name)) {
                randomEnum(BriefDepartment.DepartmentType::class.java)
            }
            .randomize(named(BriefDepartment::headOfDepartmentId.name), OBJECT_ID_RANDOMIZER)

    private val OBJECT_ID_RANDOMIZER = Randomizer { ObjectId().toString() }

    @get:Synchronized
    private val MAP_EASY_RANDOM = EasyRandom(DEFAULT_RANDOM_PARAMS)

    fun randomBriefDepartment(): BriefDepartment {
        return MAP_EASY_RANDOM.nextObject(BriefDepartment::class.java)
    }

    fun randomBriefDepartmentWithDepartmentType(departmentType: BriefDepartment.DepartmentType): BriefDepartment {
        return MAP_EASY_RANDOM.nextObject(BriefDepartment::class.java).copy(departmentType = departmentType)
    }
}
