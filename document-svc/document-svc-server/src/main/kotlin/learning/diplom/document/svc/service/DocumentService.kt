package learning.diplom.document.svc.service

import com.mongodb.client.gridfs.model.GridFSFile
import learning.diplom.document.svc.model.Document
import learning.diplom.document.svc.util.JwtUtil
import org.bson.types.Binary
import org.slf4j.LoggerFactory
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.gridfs.GridFsResource
import org.springframework.data.mongodb.gridfs.GridFsTemplate
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import reactor.kotlin.core.publisher.zip
import java.io.ByteArrayOutputStream
import java.io.FileInputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import javax.servlet.http.HttpServletResponse
import javax.ws.rs.core.MediaType

@Service
class DocumentService(
    private val gridFsTemplate: GridFsTemplate,
    private val jwtUtil: JwtUtil,
    private val mongoOperations: MongoOperations,
    private val signService: SignService
) {

    //UPDATE

    fun uploadDocument(file: MultipartFile, redisKey: String?) {
        val claims = jwtUtil.getClaims(redisKey)!!

        val document = Document(
            userId = claims["userId"].toString(),
            fileName = file.name,
            contentType = file.contentType,
            size = file.size,
            departmentId = claims["departmentId"].toString()
        )

        gridFsTemplate.store(file.inputStream, file.originalFilename, file.contentType, document)
        LOG.info("Document = {} stored", document)
    }

    fun downloadDocument(id: String, response: HttpServletResponse) {
        val gridFsResource = getGridFsResourceById(id)
        val gridFsFile = getGridFsFileById(id)

        if (gridFsFile.metadata!!["signatureExists"] == true) {
            val headerKey = HttpHeaders.CONTENT_DISPOSITION
            val headerValue = "attachment; filename=\"zipFile.zip\""
            response.setHeader(headerKey, headerValue)

            LOG.info("Download zip")
            val byteArrayOutputStream = ByteArrayOutputStream()
            val zipOutputStream = ZipOutputStream(byteArrayOutputStream)
            val gridFsResourceInputStream = gridFsResource.content
            // put document to zip
            zipOutputStream.putNextEntry(ZipEntry(gridFsFile.filename))
            zipOutputStream.write(gridFsResourceInputStream.readAllBytes())
            zipOutputStream.closeEntry()
            gridFsResourceInputStream.close()

            // put signature to zip
            zipOutputStream.putNextEntry(
                ZipEntry("department_${gridFsFile.metadata!!["departmentId"].toString()}_signature")
            )
            zipOutputStream.write((gridFsFile.metadata!!["signature"] as Binary).data)
            zipOutputStream.closeEntry()

            // put publicKey to zip
            zipOutputStream.putNextEntry(
                ZipEntry("department_${gridFsFile.metadata!!["departmentId"].toString()}_publicKey.key")
            )
            zipOutputStream.write((gridFsFile.metadata!!["publicKey"] as Binary).data)
            zipOutputStream.closeEntry()

            zipOutputStream.flush()
            zipOutputStream.close()

            response.contentType = "application/zip"
            val outputStream = response.outputStream
            outputStream.write(byteArrayOutputStream.toByteArray())
            outputStream.flush()
            outputStream.close()
        } else {
            val headerKey = HttpHeaders.CONTENT_DISPOSITION
            val headerValue = "attachment; filename=\"${gridFsFile.filename}\""
            response.setHeader(headerKey, headerValue)

            LOG.info("Download file")
            response.contentType = MediaType.APPLICATION_OCTET_STREAM
            val outputStream = response.outputStream
            outputStream.write(gridFsResource.content.readAllBytes())
            outputStream.close()
        }
    }

    fun approveDocument(redisKey: String?, documentId: String?): Boolean {
        val claims = jwtUtil.getClaims(redisKey)!!
        val gridFsFile = getGridFsFileById(documentId!!)
        val (_, approveField) = jwtUtil.getRoleWithApproveFieldFromClaims(claims) ?: return false
        gridFsFile.metadata!![approveField] = true
        mongoOperations.save(gridFsFile, "fs.files")

        signService.signDocumentIfHasConditions(gridFsFile, redisKey!!)
        return true
    }

    // READ

    fun getAllDocumentsByUserId(redisKey: String): List<GridFSFile> {
        val userId = jwtUtil.getClaims(redisKey)!!["userId"].toString()
        LOG.info("Get all documents for departmentId = {}", userId)

        val fileList: MutableList<GridFSFile> = mutableListOf()
        gridFsTemplate.find(
            Query.query(
                Criteria.where("metadata.userId").`is`(userId)
//                    .and("metadata.signatureExists").`is`(false)
            )
        ).into(fileList)

        LOG.info("Found = {}", fileList)
        return fileList
    }

    fun getGridFsResourceById(fileId: String): GridFsResource {
        LOG.info("Find gridFsResource by fileId = {}", fileId)
        val gridFsFile = getGridFsFileById(fileId)
        return gridFsTemplate.getResource(gridFsFile)
    }

    fun getGridFsFileById(fileId: String): GridFSFile {
        LOG.info("Find gridFsFile by fileId = {}", fileId)

        return gridFsTemplate.findOne(
            Query.query(
                Criteria.where("_id").`is`(fileId)
            )
        )
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(DocumentService::class.java)
    }
}
