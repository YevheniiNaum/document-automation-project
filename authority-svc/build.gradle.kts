import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val spaceUsername: String by project
val spacePassword: String by project
val modelErrorLibVersion by extra { "0.0.19-SNAPSHOT" }

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
    maven {
        url = uri("https://maven.pkg.jetbrains.space/naum-team-space/p/doc-auto/model-error-lib")
        credentials {
            username = spaceUsername
            password = spacePassword
        }
    }
}

extra["springCloudVersion"] = "2021.0.2"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb:2.6.7")
    // REDIS AND SESSION
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    // https://mvnrepository.com/artifact/org.springframework.session/spring-session-data-redis
//    implementation("org.springframework.session:spring-session-data-redis:2.7.0")
// https://mvnrepository.com/artifact/org.springframework.session/spring-session-core
    implementation("org.springframework.session:spring-session-core:2.7.0")


    implementation("org.springframework.boot:spring-boot-starter-validation:2.6.7")
    implementation("org.springframework.boot:spring-boot-starter-web:2.6.7")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf:2.6.7")
    implementation("nz.net.ultraq.thymeleaf:thymeleaf-layout-dialect:3.1.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.3")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.springframework.cloud:spring-cloud-starter-config:3.1.2")
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client:3.1.2")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign:3.1.2")
    implementation("org.springframework.cloud:spring-cloud-starter-bootstrap:3.1.2")
    implementation("org.springframework.boot:spring-boot-starter-actuator:2.6.7")

    ///PROTO
    implementation("io.grpc:grpc-protobuf:1.46.0")
    implementation("io.grpc:grpc-stub:1.46.0")

    // JWT
    implementation("io.jsonwebtoken:jjwt:0.9.1")
    implementation("javax.xml.bind:jaxb-api:2.3.1")
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")


    //MICROSERVICES LIBS
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
