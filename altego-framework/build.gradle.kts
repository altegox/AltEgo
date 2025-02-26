plugins {
    id("java-library")
    id("maven-publish")
}

group = "org.altegox"
version = "0.0.1-beta"

repositories {
    mavenCentral()
}

dependencies {
    api(project(":altego-common"))
    implementation("io.github.classgraph:classgraph:4.8.177")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            groupId = "org.altegox"
            artifactId = "altego"
            version = "0.0.1-beta"
        }
    }

    repositories {
        mavenLocal()
    }
}
