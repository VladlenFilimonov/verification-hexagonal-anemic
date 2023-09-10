plugins {
    id("postgres-conventions")
}

dependencies {
    implementation(project(":verification:verification-domain"))
    implementation(project(":commons"))
}