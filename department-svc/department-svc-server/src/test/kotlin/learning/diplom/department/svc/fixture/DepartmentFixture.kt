package learning.diplom.department.svc.fixture

import learning.diplom.department.svc.fixture.EnumFixture.randomEnum
import learning.diplom.department.svc.model.Department
import org.apache.commons.lang.RandomStringUtils.randomAlphabetic
import org.bson.types.ObjectId
import org.jeasy.random.EasyRandom
import org.jeasy.random.EasyRandomParameters
import org.jeasy.random.FieldPredicates.named
import org.jeasy.random.api.Randomizer
import kotlin.random.Random
import kotlin.random.Random.Default.nextInt

object DepartmentFixture {
    private val DEFAULT_RANDOM_PARAMS: EasyRandomParameters
        get() = EasyRandomParameters()
            .seed(Random.nextLong())
            .collectionSizeRange(1, 2)
            .overrideDefaultInitialization(true)
            .randomize(named(Department::departmentId.name), OBJECT_ID_RANDOMIZER)
            .randomize(named(Department::administrationId.name), OBJECT_ID_RANDOMIZER)
            .randomize(named(Department::name.name)) {
                randomAlphabetic(10)
            }
            .randomize(named(Department::usersLimit.name)) {
                nextInt(1, 11)
            }
            .randomize(named(Department::departmentType.name)) {
                randomEnum(Department.DepartmentType::class.java)
            }

    private val OBJECT_ID_RANDOMIZER = Randomizer { ObjectId().toString() }

    @get:Synchronized
    private val DB_EASY_RANDOM = EasyRandom(
        DEFAULT_RANDOM_PARAMS
            .excludeField(named(Department::createdDate.name))
            .excludeField(named(Department::lastModifiedDate.name))
            .excludeField(named(Department::departmentId.name))
            .excludeField(named(Department::version.name))
    )

    @get:Synchronized
    private val MAP_EASY_RANDOM = EasyRandom(DEFAULT_RANDOM_PARAMS)

    fun randomDbDepartment(): Department {
        return DB_EASY_RANDOM.nextObject(Department::class.java).copy(deleted = false)
    }

    fun randomDbDepartmentWithDepartmentType(departmentType: Department.DepartmentType): Department {
        return DB_EASY_RANDOM.nextObject(Department::class.java).copy(deleted = false, departmentType = departmentType)
    }

    fun randomDepartment(): Department {
        return MAP_EASY_RANDOM.nextObject(Department::class.java).copy(deleted = false)
    }

    fun randomDepartmentWithDepartmentType(departmentType: Department.DepartmentType): Department {
        return MAP_EASY_RANDOM.nextObject(Department::class.java).copy(deleted = false, departmentType = departmentType)
    }
}
