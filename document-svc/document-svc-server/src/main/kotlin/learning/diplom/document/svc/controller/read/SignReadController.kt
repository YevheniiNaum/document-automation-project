package learning.diplom.document.svc.controller.read

import learning.diplom.document.svc.service.SignService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/sign")
class SignReadController(
    private val signService: SignService
) {

    @GetMapping("/keys")
    fun getKeys(
        @CookieValue("info") redisKey: String,
        model: Model
    ): String {
        val privateKey = signService.getPrivateKey(redisKey)
        model.addAttribute("privateKey", privateKey)

        val publicKey = signService.getPublicKey(redisKey)
        model.addAttribute("publicKey", publicKey)
        return "keys"
    }

    @GetMapping("/verify")
    fun verifySignature(): String {
        LOG.info("Verify document page")

        return "verify-signature"
    }

    @GetMapping("/verify-result")
    fun approveDocument(
        @CookieValue("info") redisKey: String,
        @ModelAttribute @RequestParam result: Boolean,
        model: Model
    ): String {
        LOG.info("Verify-result  page")

        model.addAttribute("result", result)
        return "verify-result"
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(SignReadController::class.java)
    }
}
