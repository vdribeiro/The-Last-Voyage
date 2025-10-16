import java.util.Properties
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

val localProperties = Properties().apply { rootProject.file("local.properties").takeIf { it.exists() }?.inputStream()?.use(block = this::load) }
val appId: String = "com.hybris.tlv"
val appName: String = "The Last Voyage"
val appVersion: String = "1.0.0"
val appVersionNumber: Int = 1
val appVendor: String = "Hybris"
val appDescription: String = "An Educational Space Adventure"
val appHomepage: String = "https://mammoth-gallium-e97.notion.site/The-Last-Voyage-2420fa355a5080da91ffd9262f430feb"
val sentryDsn: String = localProperties.getProperty("sentryDsn", "")
val macIdentity: String = localProperties.getProperty("mac.sign.identity", "")

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
        val packageDir = "${taskAppId.get()}.platform"
        val objectName = "Property"
        val file = taskOutputDir.get().file("${packageDir.replace(oldChar = '.', newChar = '/')}/$objectName.kt").asFile
        file.parentFile.mkdirs()
        file.writeText(
            """
            package $packageDir

            /**
             * Generated build-time configuration values. DO NOT EDIT.
             */
            object $objectName {
                const val APP_NAME: String = "${taskAppName.get()}"
                const val APP_VERSION: String = "${taskAppVersion.get()}"
                const val SENTRY_DSN: String = "${taskSentryDsn.get()}"
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
val javafxDependencies = listOf(
    libs.javafx.base,
    libs.javafx.graphics,
    libs.javafx.media,
    libs.javafx.swing
)
val javafxModules = "javafx.base,javafx.graphics,javafx.media,javafx.swing"
val currentOS: OperatingSystem = OperatingSystem.current()
val osClassifier = when {
    currentOS.isWindows -> "win"
    currentOS.isLinux -> "linux"
    currentOS.isMacOsX -> {
        when (System.getProperty("os.arch")) {
            "aarch64" -> "mac-aarch64"
            else -> "mac"
        }
    }

    else -> error("Unsupported OS: ${System.getProperty("os.name")}")
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

    jvm(name = "desktop") {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    androidTarget {
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

        val androidMain by getting {
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
            iosTarget.binaries.framework {
                baseName = "TLV"
                isStatic = true
                version = appVersion
            }
            sourceSets.getByName("${iosTarget.name}Main").dependsOn(other = appleMain)
        }
    }

    cocoapods {
        version = appVersion
        summary = appDescription
        homepage = appHomepage
        ios.deploymentTarget = "18.6"
        podfile = project.file("../iosApp/Podfile")
        pod(name = "Sentry") {
            version = "8.55.1"
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

compose.desktop {
    application {
        mainClass = "$appId.MainKt"
        javaHome = System.getenv("JAVA_HOME").orEmpty()

        val currentOS = OperatingSystem.current()
        val isRelease = project.gradle.startParameter.taskNames.any { it.contains(other = "package", ignoreCase = true) }

        jvmArgs += "-Ddebug=${!isRelease}"
        jvmArgs += "--module-path=${javafx.asPath}"
        jvmArgs += "--add-modules=$javafxModules"

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

android {
    namespace = appId
    compileSdk = 35

    defaultConfig {
        applicationId = appId
        minSdk = 26
        targetSdk = 35
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
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
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
