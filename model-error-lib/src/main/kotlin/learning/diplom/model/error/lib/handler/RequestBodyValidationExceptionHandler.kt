package learning.diplom.model.error.lib.handler

import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class RequestBodyValidationExceptionHandler : ResponseEntityExceptionHandler() {
    companion object {
        @JvmStatic
        private val LOG = LoggerFactory.getLogger(RequestBodyValidationExceptionHandler::class.java)

        private const val REQUIRED_REQUEST_BODY_IS_MISSING = "Required request body is missing"
    }
}
