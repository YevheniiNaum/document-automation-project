package learning.diplom.gatewaysvc

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

@SpringBootApplication
@EnableDiscoveryClient
class GatewaySvcApplication

fun main(args: Array<String>) {
    runApplication<GatewaySvcApplication>(*args)
}
