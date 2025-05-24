
plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlin.plugin.serialization)
}

group = "com.example"
version = "0.0.1"

application {
    mainClass = "io.ktor.server.netty.EngineMain"
}

tasks.withType<Jar> {
    from(sourceSets.main.get().resources)
}


repositories {
    mavenCentral()
    google()
}

dependencies {
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.host.common)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.postgresql)
    implementation(libs.h2)
    implementation(libs.exposed.core)
    implementation(libs.exposed.jdbc)
    implementation(libs.ktor.server.status.pages)
    implementation(libs.ktor.server.netty)
    implementation(libs.logback.classic)
    implementation(libs.ktor.server.config.yaml)
    implementation(libs.exposed.dao)
    implementation(libs.exposed.date)
    implementation(libs.exposed.java.time)

    // integar postgres
    implementation("org.postgresql:postgresql:42.7.3")
    implementation("com.zaxxer:HikariCP:5.1.0")

    // integrar firebase
    implementation("com.google.firebase:firebase-admin:9.2.0")


    testImplementation(libs.ktor.server.test.host)
    testImplementation(libs.kotlin.test.junit)


}
