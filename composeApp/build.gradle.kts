import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(notation = libs.plugins.kotlinMultiplatform)
    alias(notation = libs.plugins.androidApplication)
    alias(notation = libs.plugins.composeMultiplatform)
    alias(notation = libs.plugins.composeCompiler)
    alias(notation = libs.plugins.sqldelight)
    alias(notation = libs.plugins.kotlinSerialization)
    alias(notation = libs.plugins.googleServices)
    alias(notation = libs.plugins.crashlytics)
}

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

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "TLV"
            isStatic = true
        }
    }

    jvm(name = "desktop")

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
            resources.srcDirs("src/commonMain/resources")
        }

        val commonTest by getting {
            dependencies {
                implementation(dependencyNotation = libs.bundles.common.test)
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(dependencyNotation = project.dependencies.platform(libs.firebase.bom))
                implementation(dependencyNotation = libs.bundles.android)
            }
        }

        val desktopMain by getting {
            dependencies {
                val os = System.getProperty("os.name").lowercase()
                val arch = System.getProperty("os.arch").lowercase()
                val classifier = when {
                    os.contains(other = "win") -> "win"
                    os.contains(other = "mac") && arch == "aarch64" -> "mac-aarch64"
                    os.contains(other = "mac") -> "mac"
                    else -> "linux"
                }

                implementation(dependencyNotation = compose.desktop.currentOs)
                implementation(dependencyNotation = libs.bundles.desktop)
                implementation(dependencyNotation = "${libs.javafx.media.get().group}:${libs.javafx.media.get().name}:${libs.javafx.media.get().version}:${classifier}")
                implementation(dependencyNotation = "${libs.javafx.swing.get().group}:${libs.javafx.swing.get().name}:${libs.javafx.swing.get().version}:${classifier}")
            }
        }
    }
}

android {
    namespace = libs.versions.applicationId.get()
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = libs.versions.applicationId.get()
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

dependencies {
    debugImplementation(compose.uiTooling)
}

compose.desktop {
    application {
        mainClass = libs.versions.applicationId.get() + ".MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = libs.versions.applicationId.get()
            packageVersion = "1.0.0"
        }
    }
}

sqldelight {
    databases {
        create("AppDatabase") {
            packageName.set("database")
            schemaOutputDirectory.set(file(path ="${project.projectDir}/src/commonMain/sqldelight/schema"))
            verifyMigrations.set(true)
        }
    }
}
