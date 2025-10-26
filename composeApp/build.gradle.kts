import java.util.Properties
import kotlin.experimental.xor
import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler
import org.gradle.internal.os.OperatingSystem

plugins {
    alias(notation = libs.plugins.kotlinMultiplatform)
    alias(notation = libs.plugins.cocoapods)
    alias(notation = libs.plugins.kover)
    alias(notation = libs.plugins.sentry)
    alias(notation = libs.plugins.androidApplication)
    alias(notation = libs.plugins.composeMultiplatform)
    alias(notation = libs.plugins.composeCompiler)
    alias(notation = libs.plugins.sqldelight)
    alias(notation = libs.plugins.kotlinSerialization)
}

//region Local Properties
val localProperties: Properties = Properties().apply { rootProject.file("local.properties").takeIf { it.exists() }?.inputStream()?.use(block = this::load) }
val appId: String = "com.hybris.tlv"
val appName: String = "The Last Voyage"
val appDescription: String = "An Educational Space Adventure"
val appVendor: String = "Hybris"
val appHomepage: String = "https://mammoth-gallium-e97.notion.site/The-Last-Voyage-2420fa355a5080da91ffd9262f430feb"
val appVersion: String = "1.0.0"
val appVersionNumber: Int = 2
val androidTarget: Int = 35
val androidKeyAlias: String = localProperties.getProperty("android.keyAlias", "")
val androidKeyPassword: String = localProperties.getProperty("android.keyPassword", "")
val androidStoreFile: File = rootProject.file(localProperties.getProperty("android.storeFile", ""))
val androidStorePassword: String = localProperties.getProperty("android.storePassword", "")
val iosTarget: String = "16.0"
val macIdentity: String = localProperties.getProperty("mac.sign.identity", "")
val iosSentryVersion: String = libs.versions.iosSentry.get()
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
    abstract val taskSentryDsn: Property<String>
    @get:OutputDirectory
    abstract val taskOutputDir: DirectoryProperty

    @TaskAction
    fun generate() {
        val appId: String = taskAppId.get()
        val appName: String = taskAppName.get()
        val appVersion: String = taskAppVersion.get()
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
    
                /**
                 * Generated build-time values.
                 */
                object $objectName {
                    const val APP_ID: String = "$appId"
                    const val APP_NAME: String = "$appName"
                    const val APP_VERSION: String = "$appVersion"
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
    taskSentryDsn.set(sentryDsn)
    taskOutputDir.set(layout.buildDirectory.dir("generated/source/property"))
}
//endregion

//region JavaFX
val javafx: Configuration by configurations.creating
val javafxModules: String = "javafx.base,javafx.graphics,javafx.media,javafx.swing"
val javafxDependencies = listOf(libs.javafx.base, libs.javafx.graphics, libs.javafx.media, libs.javafx.swing)
val javafxModulePath: String by lazy { javafx.asPath }
val currentOS: OperatingSystem = OperatingSystem.current()
val osClassifier: String = when {
    currentOS.isWindows -> "win"
    currentOS.isLinux -> "linux"
    currentOS.isMacOsX -> {
        when (System.getProperty("os.arch", "")) {
            "aarch64" -> "mac-aarch64"
            else -> "mac"
        }
    }

    else -> System.getProperty("os.name", "")
}

fun KotlinDependencyHandler.addJavaFx() {
    javafxDependencies.forEach { implementation(dependency = it.get()) { artifact { this.classifier = osClassifier } } }
}

fun DependencyHandler.addJavaFx() {
    javafxDependencies.forEach { add(configuration = "javafx", dependency = it.get()) { artifact { this.classifier = osClassifier } } }
}
//endregion

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }

    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    jvm(name = "desktop") {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    val iosTargets = listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    )

    sourceSets {
        val commonMain by getting {
            kotlin.srcDir(generatePropertiesTask.map { it.outputs.files })
            dependencies {
                implementation(dependencyNotation = compose.runtime)
                implementation(dependencyNotation = compose.foundation)
                implementation(dependencyNotation = compose.material3)
                implementation(dependencyNotation = compose.ui)
                implementation(dependencyNotation = compose.components.resources)
                implementation(dependencyNotation = compose.components.uiToolingPreview)
                implementation(dependencyNotation = compose.materialIconsExtended)
                implementation(dependencyNotation = libs.bundles.common)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(dependencyNotation = libs.bundles.common.test)
                @OptIn(ExperimentalComposeLibrary::class)
                implementation(dependencyNotation = compose.uiTest)
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(dependencyNotation = libs.bundles.android)
            }
        }

        val desktopMain by getting {
            dependencies {
                implementation(dependencyNotation = compose.desktop.currentOs)
                implementation(dependencyNotation = libs.bundles.desktop)
                addJavaFx()
            }
        }

        val desktopTest by getting {
            dependencies {
                implementation(dependencyNotation = compose.desktop.uiTestJUnit4)
            }
        }

        val appleMain by creating {
            dependsOn(other = commonMain)
            dependencies {
                implementation(dependencyNotation = libs.bundles.ios)
            }
        }
        iosTargets.forEach { iosTarget ->
            iosTarget.binaries.framework {
                baseName = "TLV"
                isStatic = true
                version = appVersion
                freeCompilerArgs += "-Xbinary=bundleId=$appId"
            }
            sourceSets.getByName("${iosTarget.name}Main").dependsOn(other = appleMain)
        }
    }

    cocoapods {
        version = appVersion
        summary = appDescription
        homepage = appHomepage
        ios.deploymentTarget = iosTarget
        podfile = project.file("../iosApp/Podfile")
        pod(name = "Sentry") {
            version = iosSentryVersion
            linkOnly = true
            extraOpts += listOf("-compiler-option", "-fmodules")
        }
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
    debugImplementation(libs.androidx.test.manifest)
    addJavaFx()
}

android {
    namespace = appId
    compileSdk = androidTarget

    signingConfigs {
        create("release") {
            keyAlias = androidKeyAlias
            keyPassword = androidKeyPassword
            storeFile = androidStoreFile
            storePassword = androidStorePassword
        }
    }
    defaultConfig {
        applicationId = appId
        minSdk = 26
        targetSdk = androidTarget
        versionCode = appVersionNumber
        versionName = appVersion
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildFeatures {
        buildConfig = true
    }
    sourceSets {
        getByName("main") {
            assets.srcDirs("src/commonMain/resources")
        }
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        debug {
            isMinifyEnabled = false
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

compose.desktop {
    application {
        mainClass = "$appId.MainKt"
        javaHome = System.getenv("JAVA_HOME").orEmpty()
        val isRelease = project.gradle.startParameter.taskNames.any { it.contains(other = "package", ignoreCase = true) }

        jvmArgs += "-Ddebug=${!isRelease}"
        if (currentOS.isMacOsX) {
            jvmArgs += "-Xdock:icon=${project.file("src/commonMain/composeResources/drawable/ic_launcher_round.icns").absolutePath}"
            jvmArgs += "-Xdock:name=$appName"
            jvmArgs += "-Dapple.awt.application.name=$appName"
        }

        nativeDistributions {
            packageName = appName
            packageVersion = appVersion
            description = appDescription
            vendor = appVendor

            targetFormats(
                TargetFormat.Dmg,
                TargetFormat.Msi,
                TargetFormat.Deb
            )

            modules("java.sql")

            macOS {
                iconFile.set(project.file("src/commonMain/composeResources/drawable/ic_launcher_round.icns"))
                bundleID = appId
                if (isRelease) {
                    entitlementsFile.set(project.file("src/desktopMain/resources/entitlements.plist"))
                    signing {
                        sign.set(true)
                        identity.set(macIdentity)
                    }
                }
            }

            windows {
                iconFile.set(project.file("src/commonMain/composeResources/drawable/ic_launcher_round.ico"))
                shortcut = true
            }

            linux {
                iconFile.set(project.file("src/commonMain/composeResources/drawable/ic_launcher_round.png"))
            }
        }
    }
}

// Module paths have to run after, otherwise it breaks the configuration
project.afterEvaluate {
    compose.desktop.application.jvmArgs += "--module-path=$javafxModulePath"
    compose.desktop.application.jvmArgs += "--add-modules=$javafxModules"
}

sqldelight {
    databases {
        create(name = "AppDatabase") {
            packageName.set("database")
            schemaOutputDirectory.set(file(path = "${project.projectDir}/src/commonMain/sqldelight/schema"))
        }
    }
}

kover {
    reports {
        total {
            html { onCheck = true }
            log { onCheck = true }
            verify {
                onCheck = true
                rule { bound { minValue = 80 } } // minimum 80% coverage
            }

            filters {
                excludes {
                    annotatedBy("kotlinx.serialization.Serializable")
                    packages(
                        "*.generated*",
                        "database",
                        "${appId}.flow",
                    )
                    classes(
                        "${appId}.Debug",
                        "${appId}.KInitializer*",
                        "${appId}.Main*",
                    )
                }
            }
        }
    }
}

// Enable native access for tests
tasks.withType<Test> {
    jvmArgs("--enable-native-access=ALL-UNNAMED")
}
