package learning.diplom.user.svc.openfeign.client

import feign.Headers
import feign.RequestLine
import learning.diplom.user.svc.config.OpenFeignConfiguration
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile
import javax.ws.rs.core.MediaType

@FeignClient("\${document-svc-feign-client}", configuration = [OpenFeignConfiguration::class])
interface DocumentSvcClient {

//    @PostMapping(
//        "/document",
//        consumes = [MediaType.MULTIPART_FORM_DATA],
//        produces = [MediaType.MULTIPART_FORM_DATA]
//    )
//    @Headers("Content-Type: multipart/form-data")
//    fun uploadDocument(
//        @RequestParam file: MultipartFile,
//        @CookieValue("info") redisKey: String? = null
//    ): ResponseEntity<String>
}
