package learning.diplom.document.svc.service

import com.mongodb.client.gridfs.GridFSBucket
import com.mongodb.client.gridfs.model.GridFSFile
import learning.diplom.document.svc.repository.SignRepository
import learning.diplom.document.svc.util.JwtUtil
import org.slf4j.LoggerFactory
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.gridfs.GridFsResource
import org.springframework.data.mongodb.gridfs.GridFsTemplate
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import reactor.kotlin.core.util.function.*
import reactor.util.function.Tuple5
import reactor.util.function.Tuples
import java.io.*
import java.math.BigInteger
import java.security.*
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.RSAPrivateKeySpec
import java.security.spec.RSAPublicKeySpec
import java.security.spec.X509EncodedKeySpec
import kotlin.math.sign

@Service
class SignService(
    private val signRepository: SignRepository,
    private val gridFsTemplate: GridFsTemplate,
    private val jwtUtil: JwtUtil,
    private val gridFsBucket: GridFSBucket,
    private val mongoOperations: MongoOperations
) {
    // WRITE

    fun generateDsaKeyPair(departmentId: String) {
        val keyPairGen = KeyPairGenerator.getInstance("DSA")
        val random: SecureRandom = SecureRandom.getInstance("SHA1PRNG")
        keyPairGen.initialize(2048, random)
        val keyPair = keyPairGen.genKeyPair()
        val fact = KeyFactory.getInstance("DSA")
        saveKeyPairToFiles(keyPair, departmentId)

        val privateFileName = "${departmentId}_private.key"
        val publicFileName = "${departmentId}_public.key"

        val privateFile = File(privateFileName)
        val publicFile = File(publicFileName)

        gridFsTemplate.store(privateFile.inputStream(), privateFileName)
        gridFsTemplate.store(publicFile.inputStream(), publicFileName)
        File(privateFileName).delete()
        File(publicFileName).delete()
        println()
    }

    private fun saveKeyPairToFiles(keyPair: KeyPair, departmentId: String) {
        val privateKey = keyPair.private
        val publicKey = keyPair.public

        // Store Public Key.
        val x509EncodedKeySpec = X509EncodedKeySpec(
            publicKey.encoded
        )
        var fos = FileOutputStream("${departmentId}_public.key")
        fos.write(x509EncodedKeySpec.encoded)
        fos.close()

        // Store Private Key.
        val pkcs8EncodedKeySpec = PKCS8EncodedKeySpec(
            privateKey.encoded
        )
        fos = FileOutputStream("${departmentId}_private.key")
        fos.write(pkcs8EncodedKeySpec.encoded)
        fos.close()
    }

    private fun loadKeyPair(filename: String, algorithm: String): KeyPair {
        // Read Public Key.
        val filePublicKey = File(filename)
        var fis = FileInputStream(filename)
        val encodedPublicKey = ByteArray(filePublicKey.length().toInt())
        fis.read(encodedPublicKey)
        fis.close()

        // Read Private Key.
        val filePrivateKey = File(filename)
        fis = FileInputStream(filename)
        val encodedPrivateKey = ByteArray(filePrivateKey.length().toInt())
        fis.read(encodedPrivateKey)
        fis.close()

        // Generate KeyPair.
        val keyFactory = KeyFactory.getInstance(algorithm)
        val publicKeySpec = X509EncodedKeySpec(
            encodedPublicKey
        )
        val publicKey = keyFactory.generatePublic(publicKeySpec)

        val privateKeySpec = PKCS8EncodedKeySpec(
            encodedPrivateKey
        )
        val privateKey = keyFactory.generatePrivate(privateKeySpec)

        return KeyPair(publicKey, privateKey)
    }

    fun generateRsaKeyPair(departmentId: String) {
        val keyPairGen = KeyPairGenerator.getInstance("RSA")
        val random: SecureRandom = SecureRandom.getInstance("SHA1PRNG")
        keyPairGen.initialize(2048, random)
        val keyPair = keyPairGen.genKeyPair()
        val fact = KeyFactory.getInstance("RSA")

        val publicKey = fact.getKeySpec(
            keyPair.public,
            RSAPublicKeySpec::class.java
        )

        val privateKey = fact.getKeySpec(
            keyPair.private,
            RSAPrivateKeySpec::class.java
        )

        val privateFileName = "${departmentId}_private.txt"
        val publicFileName = "${departmentId}_public.txt"

        saveToFile(privateFileName, privateKey.modulus, privateKey.privateExponent)
        saveToFile(publicFileName, publicKey.modulus, publicKey.publicExponent)

        val privateFile = File(privateFileName)
        val publicFile = File(publicFileName)


        gridFsTemplate.store(privateFile.inputStream(), privateFileName)
        gridFsTemplate.store(publicFile.inputStream(), publicFileName)
        File(privateFileName).delete()
        File(publicFileName).delete()
        println()
    }

    fun signDocumentIfHasConditions(gridFsFile: GridFSFile, redisKey: String) {
        val (headOfAdministrationApprove,
            deputyHeadOfAdministrationApprove,
            headOfDepartmentApprove,
            deputyHeadOfDepartmentApprove,
            employeeApprove) = gridFsFile.getApproveFields()

        val privateKey = getPrivateKey(redisKey)
        val publicKey = getPublicKey(redisKey)
        val gridFsResource = gridFsTemplate.getResource(gridFsFile)

        if (headOfDepartmentApprove
            || (deputyHeadOfDepartmentApprove && employeeApprove)
        ) {
            signDocument(gridFsFile, gridFsResource, privateKey, publicKey)
        }
    }

    fun signDocument(
        gridFsFile: GridFSFile,
        gridFsResource: GridFsResource,
        privateKey: PrivateKey,
        publicKey: PublicKey
    ) {
        LOG.info("Sign document{} ", gridFsFile.filename)

        val signature = Signature.getInstance(privateKey.algorithm)
        signature.initSign(privateKey)
        signature.update(gridFsResource.content.readAllBytes())
        val completedSignature = signature.sign()

        gridFsFile.metadata!!["signature"] = completedSignature
        gridFsFile.metadata!!["signatureExists"] = true
        gridFsFile.metadata!!["publicKey"] = publicKey.encoded

        mongoOperations.save(gridFsFile, "fs.files")
    }

    fun verifySignature(publicKey: MultipartFile, signature: MultipartFile, document: MultipartFile): Boolean {
        var verifies: Boolean
        try {
            val publicKeyFromFile = readDsaPublicKey(publicKey.inputStream)

            val signatureByPublicKey = Signature.getInstance("SHA1withDSA")
            signatureByPublicKey.initVerify(publicKeyFromFile)
            signatureByPublicKey.update(document.inputStream.readAllBytes())

            verifies = signatureByPublicKey.verify(signature.inputStream.readAllBytes())

            println("signature verifies: $verifies")
        } catch (e: Exception) {
            verifies = false
        }


        document.inputStream.close()
        signature.inputStream.close()
        publicKey.inputStream.close()
        return verifies
    }

    // READ

    fun getPrivateKey(redisKey: String): PrivateKey {
        val claims = jwtUtil.getClaims(redisKey)!!
        val departmentId = claims["departmentId"].toString()

        return findPrivateKey(departmentId)
    }

    fun getPublicKey(redisKey: String): PublicKey {
        val claims = jwtUtil.getClaims(redisKey)!!
        val departmentId = claims["departmentId"].toString()

        return findPublicKey(departmentId)
    }

    private fun saveToFile(
        fileName: String,
        mod: BigInteger, exp: BigInteger
    ) {
        val oout = ObjectOutputStream(
            BufferedOutputStream(FileOutputStream(fileName))
        )
        try {
            oout.writeObject(mod)
            oout.writeObject(exp)
        } catch (e: Exception) {
            throw InternalError()
        } finally {
            oout.close()
        }
    }

    private fun findPrivateKey(departmentId: String): PrivateKey {
        val savedPrivateKey = gridFsTemplate.findOne(
            Query.query(
                Criteria.where("filename").`is`("${departmentId}_private.key")
            )
        )

        val downloadStream = gridFsBucket.openDownloadStream("${departmentId}_private.key")
        val gridFsResource = GridFsResource(savedPrivateKey, downloadStream)

        return readDsaPrivateKey(gridFsResource.inputStream)
    }

    private fun findPublicKey(departmentId: String): PublicKey {
        val savedPrivateKey = gridFsTemplate.findOne(
            Query.query(
                Criteria.where("filename").`is`("${departmentId}_public.key")
            )
        )

        val downloadStream = gridFsBucket.openDownloadStream("${departmentId}_public.key")
        val gridFsResource = GridFsResource(savedPrivateKey, downloadStream)

        return readDsaPublicKey(gridFsResource.inputStream)
    }

    private fun readDsaPrivateKey(inputStream: InputStream): PrivateKey {
        val keyFactory = KeyFactory.getInstance("DSA")
        val privateKeySpec = PKCS8EncodedKeySpec(
            inputStream.readAllBytes()
        )
        return keyFactory.generatePrivate(privateKeySpec)
    }

    private fun readDsaPublicKey(inputStream: InputStream): PublicKey {
        val keyFactory = KeyFactory.getInstance("DSA")
        val publicKeySpec = X509EncodedKeySpec(
            inputStream.readAllBytes()
        )
        return keyFactory.generatePublic(publicKeySpec)
    }

    // RSA KEYS
//    private fun readRsaPrivateKey(fileName: String): PrivateKey {
//        val `in`: InputStream = FileInputStream(fileName)
//        val oin = ObjectInputStream(BufferedInputStream(`in`))
//        return getRsaPrivateKeyFromInputStream(oin)
//    }
//
//    private fun readRsaPublicKey(fileName: String): PublicKey {
//        val `in`: InputStream = FileInputStream(fileName)
//        val oin = ObjectInputStream(BufferedInputStream(`in`))
//        return getRsaPublicKeyFromInputStream(oin)
//    }
//    private fun readRsaPrivateKey(inputStream: InputStream): PrivateKey {
//        val oin = ObjectInputStream(BufferedInputStream(inputStream))
//        return getRsaPrivateKeyFromInputStream(oin)
//    }
//
//    private fun readRsaPublicKey(inputStream: InputStream): PublicKey {
//        val oin = ObjectInputStream(BufferedInputStream(inputStream))
//        return getRsaPublicKeyFromInputStream(oin)
//    }
//
//    private fun getRsaPublicKeyFromInputStream(oin: ObjectInputStream): PublicKey {
//        return try {
//            val m = oin.readObject() as BigInteger
//            val e = oin.readObject() as BigInteger
//            val keySpec = RSAPublicKeySpec(m, e)
//            val fact = KeyFactory.getInstance("RSA")
//            fact.generatePublic(keySpec)
//        } catch (e: java.lang.Exception) {
//            throw InternalError()
//        } finally {
//            oin.close()
//        }
//    }
//
//    private fun getRsaPrivateKeyFromInputStream(oin: ObjectInputStream): PrivateKey {
//        return try {
//            val m = oin.readObject() as BigInteger
//            val e = oin.readObject() as BigInteger
//            val keySpec = RSAPrivateKeySpec(m, e)
//            val fact = KeyFactory.getInstance("RSA")
//            fact.generatePrivate(keySpec)
//        } catch (e: java.lang.Exception) {
//            throw InternalError()
//        } finally {
//            oin.close()
//        }
//    }

    private fun GridFSFile.getApproveFields(): Tuple5<Boolean, Boolean, Boolean, Boolean, Boolean> {
        return with(this.metadata!!) {
            Tuples.of(
                this["headOfAdministrationApprove"] as Boolean,
                this["deputyHeadOfAdministrationApprove"] as Boolean,
                this["headOfDepartmentApprove"] as Boolean,
                this["deputyHeadOfDepartmentApprove"] as Boolean,
                this["employeeApprove"] as Boolean
            )
        }

    }

    companion object {
        private val LOG = LoggerFactory.getLogger(SignService::class.java)
    }
}
