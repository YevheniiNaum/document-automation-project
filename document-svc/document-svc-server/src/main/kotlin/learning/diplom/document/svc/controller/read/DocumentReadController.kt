package learning.diplom.document.svc.controller.read

import learning.diplom.document.svc.model.DocumentRequest
import learning.diplom.document.svc.service.DocumentService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/document")
class DocumentReadController(
    private val documentService: DocumentService
) {

    @GetMapping("/list")
    fun getAllDocuments(
        @CookieValue("info") redisKey: String,
        model: Model
    ): String {
        val documentList = documentService.getAllDocumentsByUserId(redisKey)
        val documentId: String? = null
        model.addAttribute("documentList", documentList)
        model.addAttribute("documentRequest", DocumentRequest())
        model.addAttribute("documentId", documentId)
        return "document-list"
    }

    @GetMapping("/upload-document")
    fun uploadDocument(
        @CookieValue("info") redisKey: String,
        model: Model
    ): String {

        return "upload-document"
    }

    @GetMapping("/approve-document")
    fun approveDocument(
        @CookieValue("info") redisKey: String,
        @ModelAttribute @RequestParam result: Boolean,
        model: Model
    ): String {
        model.addAttribute("result", result)
        return "approve-document"
    }
}
