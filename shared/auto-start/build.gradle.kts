import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id(MyPlugins.kotlinMultiplatform)
    id(Plugins.Android.library)
}
kotlin {
    androidTarget("android") {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }
    sourceSets {
        commonMain.dependencies {
            implementation(project(":shared:utils"))
        }
    }
}

android {
    compileSdk = 36
    namespace = "ir.neo.util.startup"
    defaultConfig {
        minSdk = 26
    }
}
