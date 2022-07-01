package learning.diplom.model.error.lib.handler

import learning.diplom.model.error.lib.exception.rest.RestException
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest

@ControllerAdvice
class RestExceptionHandler {

    @ExceptionHandler(value = [RestException::class])
    protected fun handle(ex: RestException, request: WebRequest): ResponseEntity<Any> {
        val messageToLog = "ErrorMessage: " + ex.message
        if (ex.cause != null) {
            LOG.error(messageToLog, ex)
        } else {
            LOG.error(messageToLog)
        }
        return ResponseEntity<Any>(ex.message, ex.statusCode)
    }

    companion object {
        @JvmStatic
        private val LOG = LoggerFactory.getLogger(RestExceptionHandler::class.java)
    }

}
