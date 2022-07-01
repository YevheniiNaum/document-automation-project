package learning.diplom.model.error.lib.handler

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class InternalExceptionHandler {

    @ExceptionHandler(value = [Exception::class])
    protected fun handle(ex: Exception): ResponseEntity<Any> {
        val messageToLog = "ErrorMessage: " + ex.message
        if (ex.cause != null) {
            LOG.error(messageToLog, ex)
        } else {
            LOG.error(messageToLog)
        }
        return ResponseEntity<Any>(ex.message, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    companion object {
        @JvmStatic
        private val LOG = LoggerFactory.getLogger(InternalExceptionHandler::class.java)
    }

}
