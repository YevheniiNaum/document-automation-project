import java.io.ByteArrayOutputStream

val spaceUsername: String by project
val spacePassword: String by project

val gitVersion: Int by lazy {
    val stdout = ByteArrayOutputStream()
    rootProject.exec {
        commandLine("git", "rev-list", "--count", "HEAD")
        standardOutput = stdout
    }
    stdout.toString().trim().toInt()
}
val modelErrorLibVersion by extra { "0.0.20-SNAPSHOT" }

plugins {
    id("org.springframework.boot") version "2.6.7"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.spring") version "1.6.21"
    id("com.gorylenko.gradle-git-properties") version "2.4.1"
}

springBoot {
    mainClass.set("learning.diplom.departmentsvc.DocumentSvcApplicationKt")
}

repositories {
    mavenCentral()
}

allprojects {
    group = "learning.diplom"
    version = "0.0.${gitVersion}-SNAPSHOT"
    extra["gitVersion"] = gitVersion
}

configurations.all {
    resolutionStrategy {
        preferProjectModules()
    }
}

configure(subprojects) {
    apply(plugin = "java-library")
    apply(plugin = "java")
    apply(plugin = "idea")
    apply(plugin = "maven-publish")

    java.sourceCompatibility = JavaVersion.VERSION_17
    java.targetCompatibility = JavaVersion.VERSION_17
    repositories {
        mavenCentral()
        mavenLocal()
        maven {
            url = uri("https://maven.pkg.jetbrains.space/naum-team-space/p/doc-auto/model-error-lib")
            credentials {
                username = spaceUsername
                password = spacePassword
            }
        }
    }
}
