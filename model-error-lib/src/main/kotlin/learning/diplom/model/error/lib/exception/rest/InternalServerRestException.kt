package learning.diplom.model.error.lib.exception.rest

import org.springframework.http.HttpStatus

class InternalServerRestException(
    details: String
): RestException(
    HttpStatus.INTERNAL_SERVER_ERROR,
    "Internal server error: $details"
)
