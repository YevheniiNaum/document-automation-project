package learning.diplom.model.error.lib.handler

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import javax.validation.ConstraintViolationException

/**
 *  will only handle constraint violations in path variables and request params for now
 */
@ControllerAdvice
class ConstraintViolationExceptionHandler {
    companion object {
        @JvmStatic
        private val LOG = LoggerFactory.getLogger(ConstraintViolationExceptionHandler::class.java)
    }

    @ExceptionHandler(value = [ConstraintViolationException::class])
    protected fun handle(ex: ConstraintViolationException, request: WebRequest): ResponseEntity<Any> {
        val errors = ex.constraintViolations.map { it.message }

        val messageToLog = "Messages: " + ex.message
        if (ex.cause != null) {
            LOG.error(messageToLog, ex)
        } else {
            LOG.error(messageToLog)
        }
        return ResponseEntity<Any>(errors, HttpStatus.BAD_REQUEST)
    }
}

