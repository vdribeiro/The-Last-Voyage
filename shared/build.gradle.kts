import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    id(id = "shared")
    alias(notation = libs.plugins.kotlin.multiplatform)
    alias(notation = libs.plugins.kotlin.serialization)
    alias(notation = libs.plugins.android.library)
}

kotlin {
    jvmToolchain(jdkVersion = jdkVersion)

    androidTarget {
        compilerOptions {
            jvmTarget.set(jvmVersion)
        }
    }

    val iosTargets = listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    )

    jvm(name = "desktop") {
        compilerOptions {
            jvmTarget.set(jvmVersion)
        }
    }

    @OptIn(ExperimentalWasmDsl::class)
    val webTarget = wasmJs {
        browser()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(dependencyNotation = libs.bundles.common.shared)
            }
        }

        getByName("commonTest") {}

        getByName("androidMain") {}

        val appleMain by creating {
            dependsOn(other = commonMain)
        }
        iosTargets.forEach { iosTarget ->
            sourceSets.getByName("${iosTarget.name}Main").dependsOn(other = appleMain)
        }

        getByName("desktopMain") {}

        val webMain by creating {
            dependsOn(commonMain)
        }
        sourceSets.getByName("${webTarget.name}Main").dependsOn(other = webMain)
    }
}

android {
    namespace = "$appId.shared"
    compileSdk = androidSdkTarget.last
    defaultConfig {
        minSdk = androidSdkTarget.first
    }
    compileOptions {
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
    }
}
