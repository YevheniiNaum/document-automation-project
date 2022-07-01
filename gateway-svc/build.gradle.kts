import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.6.7"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.spring") version "1.6.21"
}

group = "learning.diplom"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

extra["springCloudVersion"] = "2021.0.2"

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.springframework.cloud:spring-cloud-starter-gateway:3.1.2")
    implementation("org.springframework.boot:spring-boot-starter-web:2.6.7")
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client:3.1.2")
    implementation("org.springframework.cloud:spring-cloud-starter-config:3.1.2")
    implementation("org.springframework.cloud:spring-cloud-starter-bootstrap:3.1.2")

    // SECURITY
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client:2.6.7")
//    implementation("org.springframework.boot:spring-boot-starter-security:2.6.7")
//    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server:2.6.7")

//    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
    testImplementation("org.springframework.boot:spring-boot-starter-test:2.6.7")
    testImplementation("org.springframework.security:spring-security-test:5.6.3")
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
