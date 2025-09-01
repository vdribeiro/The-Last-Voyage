import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(notation = libs.plugins.kotlinMultiplatform)
    alias(notation = libs.plugins.kover)
    alias(notation = libs.plugins.androidApplication)
    alias(notation = libs.plugins.composeMultiplatform)
    alias(notation = libs.plugins.composeCompiler)
    alias(notation = libs.plugins.sqldelight)
    alias(notation = libs.plugins.kotlinSerialization)
}

val appId: String = libs.versions.applicationId.get()

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }

    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    jvm(name = "desktop") {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    sourceSets {
        val commonMain by getting {
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
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = appId
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
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
            packageVersion = "1.0.0"
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
                        "${appId}.http",
                        "${appId}.storage",
                        "${appId}.media",
                        "${appId}.locale",
                        "${appId}.preview",
                        "${appId}.ui.theme",
                        "${appId}.ui.component",
                    )
                    classes(
                        "*Screen*",
                        "*Content*",
                        "${appId}.App*",
                        "${appId}.Composable*",
                        "${appId}.KInitializer*",
                        "${appId}.Main*",
                        "${appId}.App*",
                    )
                }
            }
        }
    }
}

tasks.withType<Test> {
    jvmArgs("--enable-native-access=ALL-UNNAMED")
}
