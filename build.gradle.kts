import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
//import org.gradle.jvm.tasks.Jar


plugins {
    kotlin("jvm") version "1.3.41"
}

group = "com.johnowl"
version = "1.1.0"

repositories {
    mavenCentral()
    maven { setUrl("https://dl.bintray.com/hotkeytlt/maven") }
}


dependencies {
    implementation(kotlin("stdlib-jdk8"))
    compile("com.github.h0tk3y.betterParse:better-parse-jvm:0.4.0-alpha-3")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.3.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.3.1")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

/*val fatJar = task("fatJar", type = Jar::class) {
    from(configurations.compile.get().map({ if (it.isDirectory) it else zipTree(it) }))
    with(tasks.jar.get() as CopySpec)
}

tasks {
    "build" {
        dependsOn(fatJar)
    }
}*/