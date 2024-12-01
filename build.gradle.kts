plugins {
    java
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
    implementation(libs.commons.cli)
//    compileOnly("org.projectlombok:lombok")
//    annotationProcessor("org.projectlombok:lombok")
//    runtimeOnly("com.h2database:h2")
}

fun DependencyHandler.testDeps() {
    testImplementation(platform(libs.junit))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
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