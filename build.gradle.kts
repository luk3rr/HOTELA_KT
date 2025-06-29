import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val springSecurityVersion: String by project
val springBootVersion: String by project
val springDocVersion: String by project
val springWebVersion: String by project
val jsonWebTokenVersion: String by project
val jacksonVersion: String by project
val kotlinCoroutinesVersion: String by project
val liquibaseVersion: String by project
val kotestVersion: String by project
val kotestSpringVersion: String by project
val mockkVersion: String by project
val pactVersion: String by project

plugins {
    id("org.springframework.boot") version "3.4.4"
    id("io.spring.dependency-management") version "1.1.7"
    id("com.diffplug.spotless") version "7.0.3"
    id("org.liquibase.gradle") version "2.2.0"
    id("org.sonarqube") version "6.1.0.5360"
    kotlin("jvm") version "2.1.10"
    kotlin("plugin.spring") version "2.1.10"
    jacoco
}

group = "com.hotela"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-webflux:$springBootVersion")
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc:$springBootVersion")
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc:$springBootVersion")
    implementation("org.springframework.boot:spring-boot-starter-security:$springBootVersion")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server:$springBootVersion")
    implementation("org.springframework.security:spring-security-config:$springSecurityVersion")
    implementation("org.springframework.security:spring-security-crypto:$springSecurityVersion")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:$springDocVersion")
    implementation("io.jsonwebtoken:jjwt:$jsonWebTokenVersion")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
    implementation("com.fasterxml.jackson.core:jackson-core:$jacksonVersion")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinCoroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:$kotlinCoroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:$kotlinCoroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-slf4j:$kotlinCoroutinesVersion")
    implementation("org.liquibase:liquibase-core:$liquibaseVersion")
    implementation("org.postgresql:r2dbc-postgresql")
    implementation("org.postgresql:postgresql")
    developmentOnly("org.springframework.boot:spring-boot-devtools:$springBootVersion")
    testImplementation("org.springframework.boot:spring-boot-starter-test:$springBootVersion")
    testImplementation("org.springframework.security:spring-security-test:$springSecurityVersion")
    testImplementation("org.testcontainers:r2dbc")
    testImplementation("org.testcontainers:postgresql")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
    testImplementation("io.kotest.extensions:kotest-extensions-spring:$kotestSpringVersion")
    testImplementation("io.mockk:mockk:$mockkVersion")
    testImplementation("au.com.dius.pact:consumer:$pactVersion")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        freeCompilerArgs.set(listOf("-Xjsr305=strict"))
    }
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

tasks.withType<JacocoReport> {
    reports {
        xml.required.set(true)
    }
}

spotless {
    kotlin {
        target("src/**/*.kt")
        ktlint()
    }
    kotlinGradle {
        target("*.gradle.kts")
        ktlint()
    }
}
