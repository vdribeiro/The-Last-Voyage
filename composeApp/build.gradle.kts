import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(notation = libs.plugins.kotlinMultiplatform)
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
val sentryDsn: String = localProperties.getProperty("sentryDsn", "")

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

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }

    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
        dependencies {
            androidTestImplementation(libs.androidx.test.junit)
            debugImplementation(libs.androidx.test.manifest)
        }
    }

    jvm(name = "desktop") {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

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

        val androidInstrumentedTest by getting {
            dependsOn(other = commonTest)
        }

        val desktopMain by getting {
            dependencies {
                implementation(dependencyNotation = compose.desktop.currentOs)
                implementation(dependencyNotation = libs.bundles.desktop)

                val osName = System.getProperty("os.name")
                val osArch = System.getProperty("os.arch")
                val currentOS = org.gradle.internal.os.OperatingSystem.current()

                val jfxClassifier = when {
                    currentOS.isWindows -> "win"
                    currentOS.isLinux -> "linux"
                    currentOS.isMacOsX -> {
                        when (osArch) {
                            "aarch64" -> "mac-aarch64"
                            else -> "mac"
                        }
                    }

                    else -> error("Unsupported OS: $osName")
                }

                implementation(dependency = libs.javafx.base.get()) { artifact { this.classifier = jfxClassifier } }
                implementation(dependency = libs.javafx.graphics.get()) { artifact { this.classifier = jfxClassifier } }
                implementation(dependency = libs.javafx.media.get()) { artifact { this.classifier = jfxClassifier } }
                implementation(dependency = libs.javafx.swing.get()) { artifact { this.classifier = jfxClassifier } }
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
        val appleList = listOf(
            iosX64(),
            iosArm64(),
            iosSimulatorArm64()
        )
        appleList.forEach { iosTarget ->
            iosTarget.binaries.framework {
                baseName = "TLV"
                isStatic = true
                version = appVersion
            }
            sourceSets.getByName("${iosTarget.name}Main").dependsOn(other = appleMain)
        }
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
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

compose.desktop {
    application {
        mainClass = "$appId.MainKt"
        javaHome = System.getenv("JAVA_HOME").orEmpty()

        nativeDistributions {
            packageName = "The Last Voyage"
            packageVersion = appVersion
            description = "An Educational Space adventure."
            vendor = appId

            targetFormats(
                TargetFormat.Dmg,
                TargetFormat.Msi,
                TargetFormat.Deb
            )

            modules("java.sql")

            macOS {
                iconFile.set(project.file("src/commonMain/composeResources/drawable/ic_launcher_round.icns"))
                entitlementsFile.set(project.file("src/desktopMain/resources/entitlements.plist"))
            }
            windows {
                iconFile.set(project.file("src/commonMain/composeResources/drawable/ic_launcher_round.ico"))
                shortcut = true
            }
            linux {
                iconFile.set(project.file("src/commonMain/composeResources/drawable/ic_launcher_round.png"))
            }
        }

        val isRelease = project.gradle.startParameter.taskNames.any {
            it.contains(other = "package", ignoreCase = true)
        }
        jvmArgs += "-Ddebug=${!isRelease}"
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
            html {
                onCheck = true
            }

            log {
                onCheck = true
            }

            verify {
                onCheck = true
                rule {
                    bound {
                        minValue = 80
                    }
                }
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

tasks.withType<Test> {
    jvmArgs("--enable-native-access=ALL-UNNAMED")
}
