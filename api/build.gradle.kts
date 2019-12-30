import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.2.2.RELEASE"
    id("io.spring.dependency-management") version "1.0.8.RELEASE"
    kotlin("jvm") version "1.3.61"
    kotlin("plugin.spring") version "1.3.61"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
    mavenCentral()
}

dependencyManagement {
    imports {
        mavenBom("com.linecorp.armeria:armeria-bom:0.97.0")
        mavenBom("io.netty:netty-bom:4.1.43.Final")
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("mysql:mysql-connector-java")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    listOf("armeria",
//            "armeria-brave",
//            "armeria-grpc",
//            "armeria-jetty",
//            "armeria-kafka",
//            "armeria-logback",
//            "armeria-retrofit2",
//            "armeria-rxjava",
//            "armeria-saml",
//            "armeria-spring-boot-autoconfiguration",
            "armeria-spring-boot-webflux-starter"
//            "armeria-thrift",
//            "armeria-tomcat",
//            "armeria-zookeeper"
    ).forEach {
        implementation("com.linecorp.armeria:$it:0.97.0")
    }

    runtimeOnly("ch.qos.logback:logback-classic:1.2.3")
    runtimeOnly("org.slf4j:log4j-over-slf4j:1.7.29")

    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}
