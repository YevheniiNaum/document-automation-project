package learning.diplom.administration.svc.fixture

import kotlin.math.min

object EnumFixture {
    inline fun <reified T : Enum<T>> randomEnums(
        clazz: Class<T>,
        size: Int = 1,
        filter: (T) -> Boolean = { true }
    ): List<T> {
        return clazz.enumConstants
            .filterIsInstance<T>()
            .shuffled()
            .filter { filter(it) }
            .let {
                it.take(min(size, it.size))
            }
    }

    inline fun <reified T : Enum<T>> randomEnum(
        clazz: Class<T>,
        filter: (T) -> Boolean = { true }
    ): T {
        return randomEnums(clazz = clazz, filter = filter)[0]
    }

    inline fun <reified T : Enum<T>> randomProtoRecognizedEnums(
        clazz: Class<T>,
        size: Int = 1,
    ) = randomEnums(clazz, size) { it.isRecognized() }

    inline fun <reified T : Enum<T>> randomProtoRecognizedEnum(
        clazz: Class<T>,
    ): T {
        return randomProtoRecognizedEnums(clazz)[0]
    }

    inline fun <reified T : Enum<T>> randomOrderEnums(
        clazz: Class<T>,
        filter: (T) -> Boolean = { true }
    ): Set<T> {
        return clazz.enumConstants
            .filterIsInstance<T>()
            .shuffled()
            .filter { filter(it) }
            .toSet()
    }

    inline fun <reified T : Enum<T>> randomOrderGwRecognizedEnums(
        clazz: Class<T>,
    ): Set<T> {
        return randomOrderEnums(clazz) { it.isRecognized() }
    }

    fun Enum<*>.isRecognized(): Boolean {
        return name != "UNRECOGNIZED"
    }
}
