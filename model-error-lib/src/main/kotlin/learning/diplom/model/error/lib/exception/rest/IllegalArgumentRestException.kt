package learning.diplom.model.error.lib.exception.rest

import org.springframework.http.HttpStatus

class IllegalArgumentRestException(
    details: String
) : RestException(
    HttpStatus.BAD_REQUEST,
    "Illegal argument error: $details"
)
