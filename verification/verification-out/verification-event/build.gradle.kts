plugins {
    id("webflux-conventions")
}

dependencies {
    implementation(project(":verification:verification-domain"))
    implementation(project(":commons"))
}