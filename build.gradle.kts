import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.jfrog.bintray.gradle.BintrayExtension

plugins {
    kotlin("jvm") version "1.3.41"
    jacoco
    `maven-publish`
    id("org.jlleitschuh.gradle.ktlint") version "8.2.0"
    id("com.jfrog.bintray") version "1.8.4"
}

group = "com.johnowl"
version = "1.1.15"

repositories {
    mavenCentral()
    maven { setUrl("https://dl.bintray.com/hotkeytlt/maven") }
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("com.github.h0tk3y.betterParse:better-parse-jvm:0.4.0-alpha-3")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.3.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.3.1")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

jacoco {
    toolVersion = "0.8.2"
}

tasks {

    test {
        useJUnitPlatform()
    }

    jacocoTestReport {
        isEnabled = true
        reports {
            html.isEnabled = true
            xml.isEnabled = true
        }
    }

    jacocoTestCoverageVerification {
        violationRules {
            rule { limit { minimum = BigDecimal.valueOf(0.1) } }
        }
    }

    check {
        dependsOn(jacocoTestCoverageVerification)
        dependsOn(jacocoTestReport)
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = group.toString()
            artifactId = "owl-rules"
            version = version

            from(components["java"])
        }
    }
}

bintray {
    user = System.getenv("BINTRAY_USER")
    key = System.getenv("BINTRAY_API_KEY")
    publish = true
    setPublications("maven")
    pkg(delegateClosureOf<BintrayExtension.PackageConfig> {
        repo = "maven"
        name = "owl-rules"
        userOrg = "johnowl"
        websiteUrl = "https://blog.johnowl.com"
        githubRepo = "johnowl/owl-rules"
        vcsUrl = "https://github.com/johnowl/owl-rules"
        description = "Simple rule engine written in Kotlin"
        setLabels("kotlin")
        setLicenses("MIT")
        desc = description
    })
}