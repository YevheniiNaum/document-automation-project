package learning.diplom.model.error.lib.exception.rest

import learning.diplom.model.error.lib.ServerEntity
import org.springframework.http.HttpStatus

class EntityAlreadyChangedRestException(
    entity: ServerEntity,
    details: String
) : RestException(
    HttpStatus.CONFLICT,
    "$entity not found. Detail: $details"
)
