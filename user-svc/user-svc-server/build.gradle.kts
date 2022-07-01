import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val modelErrorLibVersion: String by rootProject.extra
val departmentSvcLibVersion: String by rootProject.extra

plugins {
    id("org.springframework.boot") version "2.6.7"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.spring") version "1.6.21"
}

repositories {

}

extra["springCloudVersion"] = "2021.0.1"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web:2.6.7")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.3")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb:2.6.7")
    implementation("org.springframework.boot:spring-boot-starter-validation:2.6.7")
    implementation("org.springframework.cloud:spring-cloud-starter-config:3.1.2")
    implementation("org.springframework.cloud:spring-cloud-starter-bootstrap:3.1.2")
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client:3.1.2")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign:3.1.2")
    // https://mvnrepository.com/artifact/io.github.openfeign.form/feign-form-spring
    implementation("io.github.openfeign.form:feign-form-spring:2.0.5")
    implementation("org.jeasy:easy-random-core:5.0.0")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf:2.6.7")

    // SECURITY
//    implementation("org.springframework.boot:spring-boot-starter-security:2.6.7")
//    implementation("org.springframework.session:spring-session-data-redis:2.7.0")
    implementation("io.lettuce:lettuce-core:6.1.8.RELEASE")
    implementation("redis.clients:jedis:4.2.3")
    implementation("org.springframework.boot:spring-boot-starter-data-redis:2.7.0")

    // JWT
    implementation("io.jsonwebtoken:jjwt:0.9.1")
    implementation("javax.xml.bind:jaxb-api:2.4.0-b180830.0359")
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")


    ///PROTO
    implementation("io.grpc:grpc-protobuf:1.46.0")
    implementation("io.grpc:grpc-stub:1.46.0")

    // MICROSERVICES LIBS
    implementation("learning.diplom:department-svc-api:$departmentSvcLibVersion")
    implementation("learning.diplom:model-error-lib:$modelErrorLibVersion")

    testImplementation("org.springframework.boot:spring-boot-starter-test:2.6.7")
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testImplementation("com.willowtreeapps.assertk:assertk-jvm:0.25")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
//    systemProperty ("eureka.client.enabled", false)
        // disable discovery
//        systemProperty("spring.cloud.discovery.enabled", false)
//        systemProperty("spring.cloud.config.discovery.enabled", false)
//        systemProperty("spring.cloud.config.enabled", false)
}
