package learning.diplom.authority.svc.controller

import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/auth")
class RestControllers {

    @GetMapping("/login/createNewSession")
    fun test(request: HttpServletRequest, response: HttpServletResponse): String {
        var sessionObj = request.getSession(false)
        //check session exist or not
        //if not available create new session
        if (sessionObj == null) {
            LOG.info("Session not available, creating new session.")
            sessionObj = request.getSession(true)
        }
        val activeSessions =
            if (sessionObj!!.getAttribute("activeSessions") != null) sessionObj.getAttribute("activeSessions")
                .toString() else "0"
        return "Session is available now with total active sessions : $activeSessions"
    }

    @GetMapping("/login/destroy-active-session")
    fun removeSession(request: HttpServletRequest, response: HttpServletResponse?): String? {
        val sessionObj = request.getSession(false)
        if (sessionObj != null) {
            sessionObj.invalidate()
            return "Session destroyed, now there are no active sessions."
        }
        return "Session not available to destroy."
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(RestControllers::class.java)
    }

}
