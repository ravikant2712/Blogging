plugins {
	java
	id("org.springframework.boot") version "3.3.1"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.rk"
version = "0.0.1-SNAPSHOT"
description = "A Simple Blogging APis"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    implementation("io.jsonwebtoken:jjwt-impl:0.11.5")
    implementation("io.jsonwebtoken:jjwt-jackson:0.11.5")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("io.swagger.core.v3:swagger-annotations:2.2.41")
    implementation("jakarta.servlet:jakarta.servlet-api:6.0.0")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0")
    testImplementation("com.h2database:h2")
    compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	runtimeOnly("com.h2database:h2")
  //  runtimeOnly("mysql:mysql-connector-java:8.1.0")
    runtimeOnly("com.mysql:mysql-connector-j:9.3.0")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
