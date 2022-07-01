package learning.diplom.authority.svc

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.ServletComponentScan
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

@SpringBootApplication
@EnableDiscoveryClient
@ServletComponentScan
class AuthoritySvcApplication

fun main(args: Array<String>) {
    runApplication<AuthoritySvcApplication>(*args)
}
