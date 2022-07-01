package learning.diplom.model.error.lib.exception.rest

import org.springframework.http.HttpStatus

open class RestException(
    val statusCode: HttpStatus,
    override val message: String,
    cause: Throwable? = null
) : RuntimeException(message, cause)
