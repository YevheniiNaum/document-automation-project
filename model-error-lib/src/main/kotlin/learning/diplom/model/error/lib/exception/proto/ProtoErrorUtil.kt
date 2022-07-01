package learning.diplom.model.error.lib.exception.proto

import learning.diplom.model.error.lib.Entity
import learning.diplom.model.error.lib.Error

object ProtoErrorUtil {
    fun createInternalServerErrorBuilder(message: String): Error.Builder {
        return Error.newBuilder()
            .setInternalServerError(
                Error.InternalServerError.newBuilder()
                    .setMessage(message)
            )
    }

    fun createEntityNotFoundErrorBuilder(entity: Entity, message: String): Error.Builder {
        return Error.newBuilder()
            .setEntityNotFoundError(
                Error.EntityNotFoundError.newBuilder()
                    .setEntity(entity)
                    .setMessage(message)
            )
    }

    fun createEntityAlreadyChangedErrorBuilder(entity: Entity, message: String): Error.Builder {
        return Error.newBuilder()
            .setEntityAlreadyChangedError(
                Error.EntityAlreadyChangedError.newBuilder()
                    .setEntity(entity)
                    .setMessage(message)
            )
    }

    fun createEntityAlreadyExistsErrorBuilder(entity: Entity, message: String): Error.Builder {
        return Error.newBuilder()
            .setEntityAlreadyExistsError(
                Error.EntityAlreadyExistsError.newBuilder()
                    .setEntity(entity)
                    .setMessage(message)
            )
    }

    fun createEntityLimitExceededErrorBuilder(entity: Entity, message: String): Error.Builder {
        return Error.newBuilder()
            .setEntityLimitExceededError(
                Error.EntityLimitExceeded.newBuilder()
                    .setEntity(entity)
                    .setMessage(message)
            )
    }

    fun createIllegalArgumentErrorBuilder(message: String): Error.Builder {
        return Error.newBuilder()
            .setIllegalArgumentError(
                Error.IllegalArgumentError.newBuilder()
                    .setMessage(message)
            )
    }
}
