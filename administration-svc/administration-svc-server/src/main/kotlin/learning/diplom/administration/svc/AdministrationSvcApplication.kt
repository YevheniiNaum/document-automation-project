package learning.diplom.administration.svc

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.mongodb.config.EnableMongoAuditing

@SpringBootApplication
@EnableMongoAuditing
class AdministrationSvcApplication

fun main(args: Array<String>) {
    runApplication<AdministrationSvcApplication>(*args)
}
