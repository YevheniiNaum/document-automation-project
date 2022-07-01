package learning.diplom.model.error.lib.exception.rest

import learning.diplom.model.error.lib.ServerEntity
import org.springframework.http.HttpStatus

class EntityAlreadyExistsRestException(
    entity: ServerEntity,
    details: String
): RestException(
    HttpStatus.CONFLICT,
    "${entity.name} already exists. Details: $details"
)
