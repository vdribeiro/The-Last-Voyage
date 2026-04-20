import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    id(id = "shared")
    alias(notation = libs.plugins.kotlin.multiplatform)
    alias(notation = libs.plugins.android.library)
}

//region Generate App.kt file
abstract class GenerateAppValuesTask: DefaultTask() {
    @get:Input
    abstract val taskAppId: Property<String>
    @get:Input
    abstract val taskAppName: Property<String>
    @get:Input
    abstract val taskAppVersion: Property<String>
    @get:Input
    abstract val taskAppVersionNumber: Property<Long>
    @get:OutputDirectory
    abstract val taskOutputDir: DirectoryProperty

    @TaskAction
    fun generate() {
        val appId: String = taskAppId.get()
        val appName: String = taskAppName.get()
        val appVersion: String = taskAppVersion.get()
        val appVersionNumber: Long = taskAppVersionNumber.get()
        val objectName = "App"
        val file = taskOutputDir.get().file("${appId.replace(oldChar = '.', newChar = '/')}/$objectName.kt").asFile
        file.parentFile.mkdirs()
        file.writeText(
            text = """
                package $appId
                
                /**
                 * Generated build-time values.
                 */
                object $objectName {
                    const val ID: String = "$appId"
                    const val NAME: String = "$appName"
                    const val VERSION: String = "$appVersion"
                    const val VERSION_NUMBER: Long = $appVersionNumber
                }
            """.trimIndent()
        )
    }
}

val generateAppValues = tasks.register<GenerateAppValuesTask>(name = "generateAppValues") {
    taskAppId.set(appId)
    taskAppName.set(appName)
    taskAppVersion.set(appVersion)
    taskAppVersionNumber.set(appVersionNumber)
    taskOutputDir.set(layout.buildDirectory.dir("generated/source/gradle"))
}
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
            kotlin.srcDir(generateAppValues.map { it.outputs.files })
            dependencies {
                implementation(dependencyNotation = libs.bundles.common.core)
            }
        }

        getByName("androidMain") {
            dependencies {
                implementation(dependencyNotation = libs.bundles.android.core)
            }
        }

        val appleMain by creating {
            dependsOn(other = commonMain)
        }
        iosTargets.forEach { iosTarget ->
            sourceSets.getByName("${iosTarget.name}Main").dependsOn(other = appleMain)
        }

        getByName("desktopMain") {
            dependencies {
                implementation(dependencyNotation = libs.bundles.desktop.core)
            }
        }

        val webMain by creating {
            dependsOn(commonMain)
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
