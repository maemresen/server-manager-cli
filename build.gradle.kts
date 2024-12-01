plugins {
    java
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)

    alias(libs.plugins.spotless)
    id("jacoco-report-aggregation")
}

spotless {
    java {
        target("**/*.java")
        googleJavaFormat(libs.versions.google.java.format.get())
    }
}

tasks.processResources {
    dependsOn(tasks.spotlessCheck)
}

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

fun DependencyHandler.productionDeps() {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation(libs.commons.cli)
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    runtimeOnly("com.h2database:h2")
}

fun DependencyHandler.testDeps() {
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testCompileOnly("org.projectlombok:lombok")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testAnnotationProcessor("org.projectlombok:lombok")
}

dependencies {
    productionDeps()
    testDeps()
}

tasks.register<Test>("integrationTest") {
    description = "Runs the integration tests."
    group = "verification"
    useJUnitPlatform {
        includeTags("int-test")
    }
}

tasks.jar {
    manifest {
        attributes(
            "Main-Class" to "com.maemresen.server.manager.cli.ServerManagerCli"
        )
    }
}

tasks.named<Test>("test") {
    useJUnitPlatform {
        excludeTags("int-test")
    }
}