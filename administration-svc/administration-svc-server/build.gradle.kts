import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val modelErrorLibVersion: String by rootProject.extra

plugins {
    id("org.springframework.boot") version "2.6.7"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.spring") version "1.6.21"
}

repositories {
    mavenCentral()

}

springBoot {
    mainClass.set("learning.diplom.departmentsvc.DepartmentSvcApplicationKt")
}

extra["springCloudVersion"] = "2021.0.2"

dependencies {
    implementation(project(":administration-svc-api"))

    implementation("org.springframework.boot:spring-boot-starter-data-mongodb:2.6.7")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf:2.6.7")
    implementation("org.springframework.boot:spring-boot-starter-web:2.6.7")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.3")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.springframework.cloud:spring-cloud-starter-config:3.1.2")
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client:3.1.2")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign:3.1.2")
    implementation("org.springframework.cloud:spring-cloud-starter-bootstrap:3.1.2")
    implementation("org.jeasy:easy-random-core:5.0.0")
    implementation("org.springframework.boot:spring-boot-starter-validation:2.6.7")

    // SECURITY
    implementation("org.springframework.boot:spring-boot-starter-security:2.6.7")
    implementation("org.springframework.session:spring-session:1.3.5.RELEASE")
    implementation("org.springframework.boot:spring-boot-starter-data-redis:2.7.0")

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

sourceSets {
    main {
        java {
            srcDirs.addAll(
                listOf(
                    file("${rootProject.projectDir}/administration-svc-api/src/generated/main/java")
                )
            )
        }
    }
}
