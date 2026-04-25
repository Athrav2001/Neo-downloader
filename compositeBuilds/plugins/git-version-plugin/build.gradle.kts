plugins {
    `kotlin-dsl`
}
repositories {
    mavenCentral()
}
version = 1
group = "ir.neo.plugin"
dependencies {
    implementation(libs.semver)
    implementation(libs.jgit)
}
gradlePlugin {
    plugins {
        create("git-version-plugin") {
            id = "ir.neo.git-version-plugin"
            implementationClass = "ir.neo.git_version.GitVersionPlugin"
        }
    }
}