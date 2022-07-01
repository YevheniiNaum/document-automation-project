package learning.diplom.document.svc.controller.update

import learning.diplom.document.svc.model.DocumentRequest
import learning.diplom.document.svc.service.DocumentService
import learning.diplom.document.svc.util.JwtUtil
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import org.springframework.web.servlet.view.RedirectView
import javax.servlet.http.HttpServletResponse
import javax.ws.rs.core.MediaType

@Controller
@RequestMapping("/document")
@CrossOrigin("*")
class DocumentUpdateController(
    private val documentService: DocumentService,
    private val jwtUtil: JwtUtil,
    @Value("\${gateway.host}")
    private val host: String? = null,
    @Value("\${gateway.port}")
    private val port: String? = null,
) {

    @PostMapping("/upload-document", consumes = [MediaType.MULTIPART_FORM_DATA])
    fun uploadDocument(
        @RequestParam file: MultipartFile,
        @CookieValue("info") redisKey: String? = null
    ): RedirectView {
        LOG.info("Started storing file = {}", file)
        documentService.uploadDocument(file, redisKey)
        return RedirectView(
            "$HTTP_PREFIX$host:$port/document/list"
        )
    }

    @PostMapping("/sign-document", consumes = [MediaType.MULTIPART_FORM_DATA])
    fun signDocument(
        @RequestParam id: String,
        @RequestBody file: MultipartFile,
        @CookieValue("info") redisKey: String? = null
    ) {
        println("fgergwhwerh \n\n$id")
    }

    @PostMapping("/approve-document")
    fun approveDocument(
        @ModelAttribute @RequestBody documentRequest: DocumentRequest? = null,
        @CookieValue("info") redisKey: String? = null,
        redirectionAttribute: RedirectAttributes
    ): RedirectView {
        LOG.info("Approve document = {}", documentRequest!!.id)
        val result = documentService.approveDocument(redisKey, documentRequest.id)
        redirectionAttribute.addAttribute("result", result)
        return RedirectView(
            "$HTTP_PREFIX$host:$port/document/approve-document"
        )
    }

    @PostMapping(
        "/download-document",
        produces = [MediaType.APPLICATION_FORM_URLENCODED],
        consumes = [MediaType.APPLICATION_FORM_URLENCODED]
    )
    fun downloadDocument(
        @ModelAttribute @RequestBody request: DocumentRequest,
        httpServletResponse: HttpServletResponse,
    ) {
        LOG.info("Download file by id = {}", request.id)
        documentService.downloadDocument(request.id!!, httpServletResponse)
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(DocumentUpdateController::class.java)
        private const val HTTP_PREFIX = "http://"
        private const val USER_ID_JWT_CLAIM = "userId"
    }
}
