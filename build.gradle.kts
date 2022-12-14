import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.21"
    application
}

group = "com.github.ghmulti"
version = "2022"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    //testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.freeCompilerArgs = listOf("-Xcontext-receivers") 
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass.set("com.github.ghmulti.MainKt")
}