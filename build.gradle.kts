import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.10"
    jacoco
    `maven-publish`
    signing
    id("org.jlleitschuh.gradle.ktlint") version "8.2.0"
}

group = "com.johnowl"
version = "1.2.1" + System.getenv("CIRCLE_BUILD_NUM")

repositories {
    mavenCentral()
    maven { setUrl("https://dl.bintray.com/hotkeytlt/maven") }
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("com.github.h0tk3y.betterParse:better-parse-jvm:0.4.1")
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

            pom {
                name.set("Owl Rules")
                description.set("Rule engine processor with a friendly programming language")
                url.set("https://github.com/johnowl/owl-rules")
                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://github.com/git/git-scm.com/blob/main/MIT-LICENSE.txt")
                    }
                }
                developers {
                    developer {
                        id.set("johnowl")
                        name.set("João Paulo Gomes")
                        email.set("johnowl@gmail.com")
                    }
                }
                scm {
                    connection.set("scm:git:git:github.com/johnowl/owl-rules.git")
                    developerConnection.set("scm:git:ssh:github.com/johnowl/owl-rules.git")
                    url.set("https://github.com/johnowl/owl-rules")
                }
            }
        }
    }
    repositories {
        maven {
            val releasesRepoUrl = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            val snapshotsRepoUrl = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
            url = if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl

            val mavenUsername: String? by project
            val mavenPassword: String? by project
            credentials {
                username = mavenUsername
                password = mavenPassword
            }
        }
    }
}

signing {
    val signingKeyId: String? by project
    val signingKey: String? by project
    val signingPassword: String? by project
    useInMemoryPgpKeys(signingKeyId, signingKey, signingPassword)
    sign(publishing.publications["maven"])
}
