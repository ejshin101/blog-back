import com.ewerk.gradle.plugins.tasks.QuerydslCompile

plugins {
    java
    id("org.springframework.boot") version "2.7.17-SNAPSHOT"
    id("io.spring.dependency-management") version "1.0.15.RELEASE"

    //querydsl plugin
    id ("com.ewerk.gradle.plugins.querydsl") version "1.0.10"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_11
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
    maven { url = uri("https://repo.spring.io/milestone") }
    maven { url = uri("https://repo.spring.io/snapshot") }
}

dependencies {
    //spring security
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")

    //JPA lombok
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("com.h2database:h2")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    developmentOnly("org.springframework.boot:spring-boot-devtools")

    //프로퍼티 값 설정 관련 의존 관계 정의
    implementation("org.springframework.boot:spring-boot-configuration-processor")

    //JAVA JWT Library
    implementation("io.jsonwebtoken:jjwt-api:0.10.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.10.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.10.5")

    //XML 문서와 JAVA 객체 간 매핑을 자동화
    implementation("javax.xml.bind:jaxb-api")

    //OAuth2
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")

    // QueryDSL
    implementation("com.querydsl:querydsl-jpa:5.0.0")
    annotationProcessor("com.querydsl:querydsl-apt:5.0.0")

    //MySQL
    runtimeOnly("com.mysql:mysql-connector-j")

    //validation 의존성 설정
    implementation("org.springframework.boot:spring-boot-starter-validation")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")

    //hex
    implementation("commons-codec:commons-codec:1.15")

    //email
    implementation("org.springframework.boot:spring-boot-starter-mail")

    //AWS S3
    implementation("org.springframework.cloud:spring-cloud-starter-aws:2.2.6.RELEASE")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

/**
 * QueryDSL Build Options
 */
val querydslDir = "src/main/generated/queryDsl"
querydsl {
    jpa = true
    querydslSourcesDir = querydslDir
}
sourceSets.getByName("main") {
    java.srcDir(querydslDir)
}
configurations {
    named("querydsl") {
        extendsFrom(configurations.compileClasspath.get())
    }
}
tasks.withType<QuerydslCompile> {
    options.annotationProcessorPath = configurations.querydsl.get()
}
