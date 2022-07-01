import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.ByteArrayOutputStream
import com.google.protobuf.gradle.*

val spaceUsername: String by project
val spacePassword: String by project

val jar: Jar by tasks
val bootJar: org.springframework.boot.gradle.tasks.bundling.BootJar by tasks

bootJar.enabled = false
jar.enabled = true

val gitVersion: Int by lazy {
    val stdout = ByteArrayOutputStream()
    rootProject.exec {
        commandLine("git", "rev-list", "--count", "HEAD")
        standardOutput = stdout
    }
    stdout.toString().trim().toInt()
}

plugins {
    id("org.springframework.boot") version "2.7.0"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.spring") version "1.6.21"
    id("com.google.protobuf") version "0.8.17"
    `maven-publish`
    `java-library`
    java
    idea
}

group = "learning.diplom"
version = "0.0.$gitVersion-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web:2.6.7")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.springframework.boot:spring-boot-starter-validation:2.6.7")

    ///PROTO
    implementation("io.grpc:grpc-protobuf:1.46.0")
    implementation("io.grpc:grpc-stub:1.46.0")
    implementation("com.google.protobuf:protobuf-javalite:3.20.1")
    //

    testImplementation("org.springframework.boot:spring-boot-starter-test:2.6.7")
    testImplementation ("com.willowtreeapps.assertk:assertk-jvm:0.25")
}

/////////////////////////////////////////
/// PUBLISHING
/////////////////////////////////////////
publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "learning.diplom"
            artifactId = "model-error-lib"
            version = "0.0.${gitVersion}-SNAPSHOT"
            from(components["kotlin"])
        }
    }
    repositories {
        maven {
            url = uri("https://maven.pkg.jetbrains.space/naum-team-space/p/doc-auto/model-error-lib")
            credentials {
                username = spaceUsername
                password = spacePassword
            }
        }
    }
}

tasks.getByName("clean") {
    doFirst { delete(file(protobuf.protobuf.generatedFilesBaseDir)) }
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

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

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
