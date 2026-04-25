plugins {
    `kotlin-dsl`
}
repositories {
    mavenCentral()
}
version = 1
group = "ir.neo.plugin"
dependencies {
    implementation("ir.neo.util:platform:1")
    implementation(libs.handlebarsJava)
}
gradlePlugin {
    plugins {
        create("installer-plugin") {
            id = "ir.neo.installer-plugin"
            implementationClass = "ir.neo.installer.InstallerPlugin"
        }
    }
}
