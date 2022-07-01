import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val modelErrorLibVersion: String by rootProject.extra

plugins {
    id("org.springframework.boot") version "2.6.7"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.spring") version "1.6.21"
}

repositories {

}

springBoot {
    mainClass.set("learning.diplom.departmentsvc.DepartmentSvcApplicationKt")
}

extra["springCloudVersion"] = "2021.0.2"

dependencies {
// https://mvnrepository.com/artifact/org.bitbucket.b_c/jose4j
    implementation("org.bitbucket.b_c:jose4j:0.7.12")


    implementation(project(":document-svc-api"))

    implementation("org.springframework.boot:spring-boot-starter-data-mongodb:2.6.7")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf:2.6.7")
    implementation("org.springframework.boot:spring-boot-starter-web:2.6.7")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.3")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.springframework.cloud:spring-cloud-starter-config:3.1.2")
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client:3.1.2")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign:3.1.2")
    // https://mvnrepository.com/artifact/io.github.openfeign.form/feign-form-spring
    implementation("io.github.openfeign.form:feign-form-spring:2.0.5")
// https://mvnrepository.com/artifact/com.nimbusds/nimbus-jose-jwt
    implementation("com.nimbusds:nimbus-jose-jwt:2.13.0")
// https://mvnrepository.com/artifact/io.projectreactor.kotlin/reactor-kotlin-extensions
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions:1.1.2")

    implementation("org.springframework.cloud:spring-cloud-starter-bootstrap:3.1.2")
    implementation("org.jeasy:easy-random-core:5.0.0")
    implementation("org.springframework.boot:spring-boot-starter-validation:2.6.7")
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
    implementation("com.google.protobuf:protobuf-javalite:3.20.1")
    //

    // MICROSERVICES LIBS
//    implementation("learning.diplom:department-svc-api:0.0.14-SNAPSHOT")
    implementation("learning.diplom:model-error-lib:$modelErrorLibVersion")

    testImplementation("org.springframework.boot:spring-boot-starter-test:2.6.7")
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
}
