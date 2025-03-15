plugins {
    id("java")
}

group = "org.altegox"
version = "0.0.1-beta"

repositories {
    mavenCentral()
}

dependencies {

    implementation(project(":altego-framework"))
    implementation(project(":api-openai"))

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}