import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("webflux-conventions")
}

dependencies {
    implementation(project(":verification:verification-domain"))
}

tasks.getByName<BootJar>("bootJar") {
    enabled = true
}