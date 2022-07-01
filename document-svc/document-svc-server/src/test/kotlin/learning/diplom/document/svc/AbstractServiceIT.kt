package learning.diplom.document.svc

import com.mongodb.client.gridfs.GridFSBucket
import learning.diplom.document.svc.repository.SignRepository
import learning.diplom.document.svc.service.DocumentService
import learning.diplom.document.svc.service.SignService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mongodb.gridfs.GridFsTemplate
import org.springframework.test.context.ActiveProfiles

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.NONE,
    properties = [
        "scheduler.enabled=true"
    ]
)
@ActiveProfiles("test, dev")
abstract class AbstractServiceIT {

    @Autowired
    lateinit var signService: SignService

    @Autowired
    lateinit var documentService: DocumentService

    @Autowired
    lateinit var signRepository: SignRepository

    @Autowired
    lateinit var gridFsTemplate: GridFsTemplate

    @Autowired
    lateinit var gridFsBucket: GridFSBucket
}
