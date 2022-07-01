package learning.diplom.model.error.lib.exception.rest

import learning.diplom.model.error.lib.ServerEntity
import org.springframework.http.HttpStatus

class EntityNotFoundRestException(
    entity: ServerEntity,
    details: String
) : RestException(
    HttpStatus.NOT_FOUND,
    "$entity not found. Detail: $details"
)
