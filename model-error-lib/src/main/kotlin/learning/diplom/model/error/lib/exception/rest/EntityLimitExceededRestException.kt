package learning.diplom.model.error.lib.exception.rest

import learning.diplom.model.error.lib.ServerEntity
import org.springframework.http.HttpStatus

class EntityLimitExceededRestException(
    entity: ServerEntity,
    details: String?
) : RestException(
    HttpStatus.CONFLICT,
    "$entity limit exceeded exception. Details: $details"
)
