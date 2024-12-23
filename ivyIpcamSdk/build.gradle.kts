import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_1_8
        }
    }

    iosArm64 {
        val ivySdkLinkerOpts = listOf("-framework", "IvySdk", "-F${rootProject.layout.projectDirectory.dir("iosApp").dir("iosApp").asFile.absolutePath}")
        compilations.named("main") {
            val ivySdkInterop by cinterops.creating {
                defFile("src/nativeInterop/cinterop/IvySdk.def")
                compilerOpts(ivySdkLinkerOpts)
            }
        }

        binaries.all {
            linkerOpts(ivySdkLinkerOpts)
        }
    }

    applyDefaultHierarchyTemplate()

    sourceSets {
        all {
            languageSettings {
                optIn("kotlinx.cinterop.BetaInteropApi")
                optIn("kotlinx.cinterop.ExperimentalForeignApi")
            }
        }

        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.ui)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(compose.components.resources)

            implementation(libs.kotlin.coroutines)
            implementation(libs.kotlin.datetime)
            implementation(libs.kotlin.serialization.json)
        }

        androidMain.dependencies {
            api(fileTree("dir" to "libs", "include" to "*.aar"))
        }
    }
}

android {
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    buildToolsVersion = libs.versions.android.buildTools.get()

    namespace = "com.ivyiot.ipcam_sdk"

    defaultConfig {
        minSdk = 21
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

compose.resources {
    packageOfResClass = "com.ivyiot.ipcam_sdk.resources"
}
