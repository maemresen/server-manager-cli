plugins {
    java
    alias(libs.plugins.spotless)
    id("jacoco-report-aggregation")
    alias(libs.plugins.shadow)
}

spotless {
    java {
        target("**/*.java")
        googleJavaFormat(libs.versions.google.java.format.get())
    }
}

tasks.processResources {
    dependsOn(tasks.spotlessApply, tasks.spotlessCheck)
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
    implementation(libs.commons.cli)
    implementation(libs.h2database)
    implementation(libs.logback.core)
    implementation(libs.logback.classic)
    implementation(libs.slf4j.api)
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
}

fun DependencyHandler.testDeps() {
    testImplementation(libs.assertj)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.junit.jupiter)
    testImplementation(platform(libs.junit))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testCompileOnly(libs.lombok)
    testAnnotationProcessor(libs.lombok)
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

    finalizedBy(tasks.shadowJar)
}

tasks.named<Test>("test") {
    useJUnitPlatform {
        excludeTags("int-test")
    }
}