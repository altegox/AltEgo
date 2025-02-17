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
        implementation("io.reactivex.rxjava3:rxjava:3.1.10")
        implementation("com.google.code.gson:gson:2.12.1")
        implementation("com.squareup.okhttp3:okhttp:4.12.0")

        testImplementation(platform("org.junit:junit-bom:5.10.0"))
        testImplementation("org.junit.jupiter:junit-jupiter")
    }
}