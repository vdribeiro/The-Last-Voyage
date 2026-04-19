import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(notation = libs.plugins.kotlin.multiplatform)
    alias(notation = libs.plugins.android.library)
}

//region Properties
val appId: String = "com.hybris.tlv"

val jdkVersion = 21
val jvmVersion = JvmTarget.JVM_21
val javaVersion = JavaVersion.VERSION_21

val androidSdkTarget: IntRange = 26..36
//endregion

kotlin {
    jvmToolchain(jdkVersion = jdkVersion)

    compilerOptions {
        freeCompilerArgs.addAll("-Xexpect-actual-classes")
    }

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
                implementation(dependencyNotation = libs.kotlin.stdlib)
                implementation(dependencyNotation = libs.bundles.common)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(dependencyNotation = libs.bundles.common.test)
            }
        }

        getByName("androidMain") {
            dependencies {
                implementation(dependencyNotation = libs.bundles.android)
            }
        }

        val appleMain by creating {
            dependsOn(other = commonMain)
            dependencies {
                implementation(dependencyNotation = libs.bundles.ios)
            }
        }
        iosTargets.forEach { iosTarget ->
            sourceSets.getByName("${iosTarget.name}Main").dependsOn(other = appleMain)
        }

        getByName("desktopMain") {
            dependencies {
                implementation(dependencyNotation = libs.bundles.desktop)
            }
        }

        val webMain by creating {
            dependsOn(commonMain)
            dependencies {
                implementation(dependencyNotation = libs.bundles.web)
            }
        }
        sourceSets.getByName("${webTarget.name}Main").dependsOn(other = webMain)
    }
}

android {
    namespace = "$appId.core"
    compileSdk = androidSdkTarget.last
    defaultConfig {
        minSdk = androidSdkTarget.first
    }
    compileOptions {
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
    }
}
