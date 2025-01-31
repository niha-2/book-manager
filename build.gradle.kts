plugins {
	kotlin("jvm") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
	id("org.springframework.boot") version "3.4.2"
	id("io.spring.dependency-management") version "1.1.7"

	// MyBatisGenerator
	id("com.thinkimi.gradle.MybatisGenerator") version "2.4"
}

group = "com.book.manager"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	// Kotlin, Spring Boot
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")

	// MyBatis
	implementation("org.mybatis.spring.boot:mybatis-spring-boot-starter:3.0.4")
	 // MyBatis Dynamic SQL
	implementation("org.mybatis.dynamic-sql:mybatis-dynamic-sql:1.2.1")
	 // MySQL Connector/J
	implementation("mysql:mysql-connector-java:8.0.23")
	 // MyBatis Generator
	mybatisGenerator("org.mybatis.generator:mybatis-generator-core:1.4.0")

	// Spring Security
	implementation("org.springframework.boot:spring-boot-starter-security")

	// Redis
	implementation("org.springframework.session:spring-session-data-redis")
	implementation("redis.clients:jedis")

	// Spring AOP
	implementation("org.springframework.boot:spring-boot-starter-aop")

	testImplementation("org.springframework.boot:spring-boot-starter-test")

	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testImplementation("org.mybatis.spring.boot:mybatis-spring-boot-starter-test:3.0.4")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

	// JUnit
	testImplementation("org.junit.jupiter:junit-jupiter-engine:5.11.4")
	// AssertJ
	testImplementation("org.assertj:assertj-core:3.27.3")

	// mockito
	testImplementation("org.mockito:mockito-core:5.15.2")
//	testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0")
	testImplementation("org.mockito.kotlin:mockito-kotlin:5.4.0")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

mybatisGenerator {
	verbose = true
	configFile = "${projectDir}/src/main/resources/generatorConfig.xml"
}
