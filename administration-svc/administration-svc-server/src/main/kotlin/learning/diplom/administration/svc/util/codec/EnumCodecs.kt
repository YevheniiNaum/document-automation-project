package learning.diplom.administration.svc.util.codec

inline fun <reified V : Enum<V>> Enum<*>?.asEnumOf(): V? = this?.let { java.lang.Enum.valueOf(V::class.java, it.name) }
inline fun <reified V : Enum<V>> Enum<*>?.safeAsEnumOf(): V? {
    return try {
        this.asEnumOf<V>()
    } catch (e: IllegalArgumentException) {
        null
    }
}

inline fun <reified V : Enum<V>> List<Enum<*>>.mapAsEnumOf(): List<V?> {
    return map {
        it.asEnumOf<V>()
    }
}
