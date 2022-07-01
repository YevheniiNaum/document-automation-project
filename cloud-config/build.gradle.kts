import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val spaceUsername: String by project
val spacePassword: String by project

plugins {
    id("org.springframework.boot") version "2.6.7"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.spring") version "1.6.21"
    id("maven-publish")
}

group = "learning.diplom"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
    mavenLocal()
    maven {
        url = uri("https://maven.pkg.jetbrains.space/naum-team-space/p/doc-auto/cloud-config")
        credentials {
            username = spaceUsername
            password = spacePassword
        }
    }
}

extra["springCloudVersion"] = "2021.0.1"

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.springframework.cloud:spring-cloud-config-server:3.1.2")
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client:3.1.2")
    implementation("org.springframework.boot:spring-boot-starter-data-redis:2.7.0")
//    implementation("org.springframework.cloud:spring-cloud-starter-zuul:1.4.7.RELEASE")

//    implementation("org.springframework.boot:spring-boot-starter-security:2.6.7")
//    implementation("org.springframework.security:spring-security-test:5.6.3")
    // SECURITY
//    implementation("org.springframework.boot:spring-boot-starter-security:2.6.7")

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

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "learning.diplom"
            artifactId = "cloud-config"
            version = "0.1-SNAPSHOT"
            from(components["java"])
        }
    }
    repositories {
        maven {
            url = uri("https://maven.pkg.jetbrains.space/naum-team-space/p/doc-auto/cloud-config")
            credentials {
                username = spaceUsername
                password = spacePassword
            }
        }
    }
}
