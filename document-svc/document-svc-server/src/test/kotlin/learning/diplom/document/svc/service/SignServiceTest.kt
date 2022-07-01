package learning.diplom.document.svc.service

import learning.diplom.document.svc.AbstractServiceIT
import org.junit.jupiter.api.Test

internal class SignServiceTest: AbstractServiceIT() {

    @Test
    fun generateKeyPair() {
//        val departmentId = ObjectId().toString()
//        val sign = signService.generateRsaKeyPair("62950cffc47ca8267f8a73a4")

        signService.generateDsaKeyPair("62950cffc47ca8267f8a73a4")

//        println(signService.)
    }


}
