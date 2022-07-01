package learning.diplom.document.svc.service

import learning.diplom.document.svc.AbstractServiceIT
import org.junit.jupiter.api.Test

internal class DocumentServiceTest: AbstractServiceIT() {

    @Test
    fun uploadDocument() {
    }

    @Test
    fun getAllDocuments() {
        val redisKey = "$2a$10\$XE6P8ZDS.kIipkLyUt0xVO1YxkIz.0Pq4UZtQ.RpeFnnOt7JrgB1G"

        documentService.getAllDocumentsByUserId(redisKey)

    }
}
