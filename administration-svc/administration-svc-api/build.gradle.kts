import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.google.protobuf.gradle.*

val spaceUsername: String by project
val spacePassword: String by project

val modelErrorLibVersion: String by rootProject.extra

val jar: Jar by tasks
val bootJar: org.springframework.boot.gradle.tasks.bundling.BootJar by tasks

bootJar.enabled = false
jar.enabled = true

plugins {
    id("org.springframework.boot") version "2.6.7"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.spring") version "1.6.21"
    id("com.google.protobuf") version "0.8.17"
}

repositories {

}

extra["springCloudVersion"] = "2021.0.2"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb:2.6.7")
    implementation("org.springframework.boot:spring-boot-starter:2.6.7")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.3")

    ///PROTO
    implementation("io.grpc:grpc-protobuf:1.46.0")
    implementation("io.grpc:grpc-stub:1.46.0")
    implementation("com.google.protobuf:protobuf-javalite:3.20.1")
    protobuf("learning.diplom:model-error-lib:$modelErrorLibVersion")

    //
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

tasks.withType<Wrapper> {
    gradleVersion = "7.2"
}
tasks.getByName("clean") {
    doFirst { delete(file(protobuf.protobuf.generatedFilesBaseDir)) }
}

/////////////////////////////////////////
/// PUBLISHING
/////////////////////////////////////////
publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "learning.diplom"
            artifactId = "administration-svc-api"
            version = "0.0.${extra["gitVersion"]}-SNAPSHOT"
            from(components["kotlin"])
        }
    }
    repositories {
        maven {
            url = uri("https://maven.pkg.jetbrains.space/naum-team-space/p/doc-auto/administration-svc-api")
            credentials {
                username = spaceUsername
                password = spacePassword
            }
        }
    }
}

val cleanAndGenerateProto by tasks.registering {
    delete(file(protobuf.protobuf.generatedFilesBaseDir))
    finalizedBy("generateProto")
}

tasks.named("publish") {
    mustRunAfter(cleanAndGenerateProto)
}
/////////////////////////////////////////
/////////////////////////////////////////

/////////////////////////////////////////
/// PROTOBUF
/////////////////////////////////////////
protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.6.1"
    }
    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:1.15.1"
        }
    }
    generatedFilesBaseDir = "$projectDir/src/generated"
    generateProtoTasks {
        all().forEach {
            it.plugins {}
        }
    }
}

sourceSets {
    main {
        java {
            srcDirs.addAll(
                listOf(
                    file("${protobuf.protobuf.generatedFilesBaseDir}/main/java"),
                    file("${projectDir}src/main/proto")
                )
            )
        }
    }
}
