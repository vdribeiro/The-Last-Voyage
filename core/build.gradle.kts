import java.util.Properties
import kotlin.apply
import kotlin.experimental.xor
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    id(id = "shared")
    alias(notation = libs.plugins.kotlin.multiplatform)
    alias(notation = libs.plugins.android.library)
}

//region Properties
val localProperties: Properties = Properties().apply {
    runCatching { rootProject.file("local.properties").takeIf { it.exists() }?.inputStream()?.use(block = this::load) }.getOrNull()
}

val sentryDsn: String = localProperties.getProperty("sentryDsn", "")
//endregion

//region Generate Property.kt file
abstract class GeneratePropertiesTask: DefaultTask() {
    @get:Input
    abstract val taskAppId: Property<String>
    @get:Input
    abstract val taskAppName: Property<String>
    @get:Input
    abstract val taskAppVersion: Property<String>
    @get:Input
    abstract val taskAppVersionNumber: Property<Long>
    @get:Input
    abstract val taskSentryDsn: Property<String>
    @get:OutputDirectory
    abstract val taskOutputDir: DirectoryProperty

    @TaskAction
    fun generate() {
        val appId: String = taskAppId.get()
        val appName: String = taskAppName.get()
        val appVersion: String = taskAppVersion.get()
        val appVersionNumber: Long = taskAppVersionNumber.get()
        // Basic obfuscation of Sentry DSN
        val sentryDsn = "byteArrayOf(${
            taskSentryDsn.get().toByteArray().mapIndexed { index, byte -> byte.xor(other = appId[index % appId.length].code.toByte()) }.joinToString(separator = ", ") { it.toString() }
        }).mapIndexed { index, byte -> byte.xor(other = APP_ID[index % APP_ID.length].code.toByte()) }.toByteArray().decodeToString()"
        val packageDir = "$appId.platform"
        val objectName = "Property"
        val file = taskOutputDir.get().file("${packageDir.replace(oldChar = '.', newChar = '/')}/$objectName.kt").asFile
        file.parentFile.mkdirs()
        file.writeText(
            text = """
                package $packageDir
                
                import kotlin.experimental.xor
                import com.hybris.tlv.test.ExcludeFromTesting
    
                /**
                 * Generated build-time values.
                 */
                @ExcludeFromTesting
                object $objectName {
                    const val APP_ID: String = "$appId"
                    const val APP_NAME: String = "$appName"
                    const val APP_VERSION: String = "$appVersion"
                    const val APP_VERSION_NUMBER: Long = $appVersionNumber
                    val sentry: String = $sentryDsn
                }
            """.trimIndent()
        )
    }
}

val generatePropertiesTask = tasks.register<GeneratePropertiesTask>(name = "generateProperties") {
    taskAppId.set(appId)
    taskAppName.set(appName)
    taskAppVersion.set(appVersion)
    taskAppVersionNumber.set(appVersionNumber)
    taskSentryDsn.set(sentryDsn)
    taskOutputDir.set(layout.buildDirectory.dir("generated/source/property"))
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
            kotlin.srcDir(generatePropertiesTask.map { it.outputs.files })
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
