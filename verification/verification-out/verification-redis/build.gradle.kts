plugins {
    id("redis-conventions")
}

dependencies {
    implementation(project(":verification:verification-domain"))
    implementation(project(":commons"))
}