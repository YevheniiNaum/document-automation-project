package learning.diplom.model.error.lib.mapper

import learning.diplom.model.error.lib.Entity
import learning.diplom.model.error.lib.Error
import learning.diplom.model.error.lib.ServerEntity
import learning.diplom.model.error.lib.exception.rest.RestException
import learning.diplom.model.error.lib.exception.rest.*
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus

object ErrorMapper {

    private val LOG = LoggerFactory.getLogger(ErrorMapper::class.java)

    fun Error.mapToRestException(): RestException {
        LOG.info("Map proto error = {} to rest exception", this)
        return when {
            hasInternalServerError() -> {
                with(internalServerError) {
                    InternalServerRestException(message)
                }
            }

            hasEntityNotFoundError() -> {
                with(entityNotFoundError) {
                    EntityNotFoundRestException(mapEntity(entity), this.message)
                }
            }

            hasEntityAlreadyChangedError() -> {
                with(entityAlreadyChangedError) {
                    EntityAlreadyChangedRestException(mapEntity(entity), this.message)
                }
            }

            hasEntityAlreadyExistsError() -> {
                with(entityAlreadyExistsError) {
                    EntityAlreadyExistsRestException(mapEntity(entity), this.message)
                }
            }

            hasIllegalArgumentError() -> {
                with(illegalArgumentError) {
                    IllegalArgumentRestException(this.message)
                }
            }

            hasEntityLimitExceededError() -> {
                with(entityLimitExceededError) {
                    EntityLimitExceededRestException(mapEntity(entity), this.message)
                }
            }

            else -> {
                LOG.warn("Unknown proto error type: {} ", this)
                RestException(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown error")
            }

        }
    }

    private fun mapEntity(protoEntity: Entity): ServerEntity {
        return when (protoEntity) {
            Entity.USER -> ServerEntity.USER
            Entity.DEPARTMENT -> ServerEntity.DEPARTMENT
            Entity.ADMINISTRATION -> ServerEntity.ADMINISTRATION
            Entity.DOCUMENT -> ServerEntity.DOCUMENT
            Entity.TOKEN -> ServerEntity.TOKEN

            else -> ServerEntity.UNKNOWN_ENTITY
        }
    }
}
