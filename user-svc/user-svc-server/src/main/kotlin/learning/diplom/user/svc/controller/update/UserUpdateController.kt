package learning.diplom.user.svc.controller.update

import learning.diplom.user.svc.model.User
import learning.diplom.user.svc.openfeign.client.DocumentSvcClient
import learning.diplom.user.svc.service.UserService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import javax.validation.Valid
import javax.ws.rs.core.MediaType

@RestController
@Validated
@RequestMapping("/user")
class UserUpdateController(
    private val userService: UserService,
    private val documentSvcClient: DocumentSvcClient
) {

    @PostMapping("/create")
    fun createUser(@Valid @RequestBody user: User): ResponseEntity<User> {
        LOG.info("Create user: user = {}", user)
        return ResponseEntity(userService.createUser(user), HttpStatus.CREATED)
    }

//    @PostMapping("/document/upload", consumes = [MediaType.MULTIPART_FORM_DATA])
//    fun uploadDocument(
//        @RequestParam file: MultipartFile,
//        @CookieValue("info") redisKey: String? = null
//    ): ResponseEntity<String> {
//        LOG.info("Started upload file = {}")
//        return documentSvcClient.uploadDocument(file, redisKey)
//    }

    companion object {
        private val LOG = LoggerFactory.getLogger(UserUpdateController::class.java)
    }
}
