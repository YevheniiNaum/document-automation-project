package learning.diplom.document.svc.controller.update

import learning.diplom.document.svc.service.DocumentService
import learning.diplom.document.svc.service.SignService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import org.springframework.web.servlet.view.RedirectView
import javax.ws.rs.core.MediaType

@RestController
@RequestMapping("/sign")
class SignUpdateController(
    private val signService: SignService,
    private val documentService: DocumentService,
    @Value("\${gateway.host}")
    private val host: String? = null,
    @Value("\${gateway.port}")
    private val port: String? = null
) {

    @PostMapping("")
    fun generateKeyPairForDepartment(departmentId: String): ResponseEntity<String> {
        LOG.info("Generate key pair for department = {}", departmentId)
        signService.generateRsaKeyPair(departmentId)
        return ResponseEntity.ok().body("SUCCESS")
    }

    @PostMapping("/download-private-key")
    fun downloadPrivateKey() {

    }

    @PostMapping(
        "/verify",
        produces = [MediaType.MULTIPART_FORM_DATA],
        consumes = [MediaType.MULTIPART_FORM_DATA]
    )
    fun verifySignature(
        @ModelAttribute @RequestBody document: MultipartFile,
        @ModelAttribute @RequestBody signature: MultipartFile,
        @ModelAttribute @RequestBody publicKey: MultipartFile,
        @CookieValue("info") redisKey: String? = null,
        redirectionAttribute: RedirectAttributes
    ): RedirectView {
        LOG.info("Verify signature {} by publicKey {} for document", signature, publicKey, document.name)
        val result = signService.verifySignature(publicKey, signature, document)
        redirectionAttribute.addAttribute("result", result)
        return RedirectView(
            "${HTTP_PREFIX}$host:$port/sign/verify-result"
        )
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(SignUpdateController::class.java)
        private const val HTTP_PREFIX = "http://"
    }
}
