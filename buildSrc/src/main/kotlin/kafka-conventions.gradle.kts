plugins {
    id("webflux-conventions")
}

dependencies {
    implementation("org.springframework.kafka:spring-kafka")
    implementation("io.projectreactor.kafka:reactor-kafka:1.3.20")

}