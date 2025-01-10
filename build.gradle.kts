plugins {
    id("java")
}

group = "org.rangenx"
version = "0.0.1-beta"

subprojects {
    apply(plugin = "java")

    repositories {
        mavenCentral()
    }

    dependencies {
        implementation("org.apache.commons:commons-lang3:3.17.0")
        implementation(platform("org.apache.logging.log4j:log4j-bom:2.24.1"))
        implementation("org.apache.logging.log4j:log4j-api:2.24.1")
        implementation("org.apache.logging.log4j:log4j-core:2.24.1")

        testImplementation(platform("org.junit:junit-bom:5.10.0"))
        testImplementation("org.junit.jupiter:junit-jupiter")
    }
}