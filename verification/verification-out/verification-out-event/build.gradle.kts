plugins {
    id("kafka-conventions")
}

dependencies {
    implementation(project(":verification:verification-domain"))
    implementation(project(":commons"))
}