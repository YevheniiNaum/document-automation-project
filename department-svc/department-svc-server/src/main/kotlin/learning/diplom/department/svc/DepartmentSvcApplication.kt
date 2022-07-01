package learning.diplom.department.svc

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.data.mongodb.config.EnableMongoAuditing

@SpringBootApplication
@EnableFeignClients
@EnableMongoAuditing
class DepartmentSvcApplication

fun main(args: Array<String>) {
    runApplication<DepartmentSvcApplication>(*args)
}
