plugins {
    id("database-conventions")
}

dependencies {
    implementation(project(":verification:verification-domain"))
    implementation(project(":commons"))
}