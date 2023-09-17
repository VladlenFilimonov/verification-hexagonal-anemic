plugins {
    id("redis-conventions")
    id("itest-conventions")
}

dependencies {
    implementation(project(":verification:verification-domain"))
    implementation(project(":commons"))

    itestImplementation(project(":verification:verification-out:verification-cache"))
    itestImplementation(project(":verification:verification-domain"))

}