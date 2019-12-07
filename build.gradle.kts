import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jfrog.gradle.plugin.artifactory.dsl.PublisherConfig
import groovy.lang.GroovyObject

plugins {
    kotlin("jvm") version "1.3.41"
    jacoco
    `maven-publish`
    id("org.jlleitschuh.gradle.ktlint") version "8.2.0"
    id("com.jfrog.artifactory") version "4.9.10"
}

group = "com.johnowl"
version = "1.1." + System.getenv("CIRCLE_BUILD_NUM")

repositories {
    mavenCentral()
    maven { setUrl("https://dl.bintray.com/hotkeytlt/maven") }
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("com.github.h0tk3y.betterParse:better-parse-jvm:0.4.0-alpha-3")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.3.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.3.1")

    implementation(gradleKotlinDsl())
    implementation(gradleApi())
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

artifactory {
    setProperty("contextUrl", "https://api.bintray.com/content/johnowl/maven/")
    publish(delegateClosureOf<PublisherConfig> {

        repository(delegateClosureOf<GroovyObject> {
            setProperty("username", System.getenv("BINTRAY_USER"))
            setProperty("username", System.getenv("BINTRAY_API_KEY"))
        })

        defaults(delegateClosureOf<GroovyObject> {
            invokeMethod("publications", publishing.publications.names.toTypedArray())
        })
    })
}
