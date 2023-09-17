import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("webflux-conventions")
}

dependencies {
    implementation(project(":verification:verification-domain"))
    implementation(project(":verification:verification-in:verification-in-rest"))
    implementation(project(":verification:verification-out:verification-cache"))
    implementation(project(":verification:verification-out:verification-database"))
    implementation(project(":verification:verification-out:verification-out-event"))
}

tasks.getByName<BootJar>("bootJar") {
    enabled = true
}

tasks.getByName<Jar>("jar") {
    enabled = false
}