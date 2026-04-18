import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    alias(notation = libs.plugins.kotlin.multiplatform)
    alias(notation = libs.plugins.android.kotlin.multiplatform.library)
    alias(notation = libs.plugins.android.lint)
}

//region Properties
val appId: String = "com.hybris.tlv.core"
val appFramework = "TLV"
val appVersion: String = "1.2.0"

val jdkVersion = 21
val jvmVersion = JvmTarget.JVM_21

val androidSdkTarget: IntRange = 26..36
//endregion

kotlin {
    jvmToolchain(jdkVersion = jdkVersion)

    compilerOptions {
        freeCompilerArgs.addAll("-Xexpect-actual-classes")
    }

    android {
        namespace = appId
        minSdk = androidSdkTarget.first
        compileSdk = androidSdkTarget.last
    }

    val iosTargets = listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).apply {
        forEach { iosTarget ->
            iosTarget.binaries.framework {
                baseName = appFramework
                isStatic = true
                version = appVersion
                freeCompilerArgs += "-Xbinary=bundleId=$appId"
            }
        }
    }

    jvm(name = "desktop") {
        compilerOptions {
            jvmTarget.set(jvmVersion)
        }
    }

    @OptIn(ExperimentalWasmDsl::class)
    val webTarget = wasmJs {
        outputModuleName = appFramework
        browser {
            commonWebpackConfig {
                outputFileName = "tlv.js"
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                    static(directory = "build/processedResources/wasmJs/main")
                }
                showProgress = true
                cssSupport {
                    enabled.set(true)
                }
            }
        }
        binaries.executable()
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
