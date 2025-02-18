plugins {
    id("java")
}

group = "org.rangenx"
version = "0.0.1-beta"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":rangenx-common"))
    implementation("io.github.classgraph:classgraph:4.8.177")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}