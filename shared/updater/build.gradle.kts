import org.gradle.kotlin.dsl.implementation
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id(MyPlugins.kotlinMultiplatform)
    id(Plugins.Android.library)
    id(Plugins.Kotlin.serialization)
}
kotlin {
    androidTarget("android") {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlin.serialization.json)
            api(libs.okhttp.okhttp)
            api(libs.kotlin.coroutines.core)
            implementation(project(":shared:utils"))
            implementation(libs.semver)
            implementation("ir.neo.util:platform:1")
        }
    }
}
android {
    namespace = "com.neo.downloader.updater"
    compileSdk = 36
    defaultConfig {
        minSdk = 26
    }
}
