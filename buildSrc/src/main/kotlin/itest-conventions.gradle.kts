plugins {
    `jvm-test-suite`
    id("spring-conventions")
}

testing {
    suites {

        withType(JvmTestSuite::class).matching { it.name in listOf("itest") }.configureEach {
            dependencies {
                implementation("org.springframework.boot:spring-boot-starter-test")
                implementation("io.projectreactor:reactor-test")
                implementation("io.kotest:kotest-runner-junit5:5.7.2")
                implementation("io.kotest.extensions:kotest-extensions-spring:1.1.3")
                implementation("org.testcontainers:testcontainers:1.19.0")
            }
        }

        val itest by registering(JvmTestSuite::class) {
            useKotlinTest()

            sources {
                java {
                    setSrcDirs(listOf("src/itest/kotlin"))
                    resources.setSrcDirs(listOf("src/itest/resources"))
                }
            }
        }
    }
}