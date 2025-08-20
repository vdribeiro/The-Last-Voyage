import java.util.Properties
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(notation = libs.plugins.kotlinMultiplatform)
    alias(notation = libs.plugins.kover)
    alias(notation = libs.plugins.androidApplication)
    alias(notation = libs.plugins.composeMultiplatform)
    alias(notation = libs.plugins.composeCompiler)
    alias(notation = libs.plugins.sqldelight)
    alias(notation = libs.plugins.kotlinSerialization)
    alias(notation = libs.plugins.googleServices)
    alias(notation = libs.plugins.crashlytics)
}

val appId: String = libs.versions.applicationId.get()

val localProperties: Properties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) localPropertiesFile.inputStream().use { input -> localProperties.load(input) }
val localPropertiesDir = layout.buildDirectory.dir("generated/secrets")

tasks.register("generateLocalProperties") {
    group = "build"

    outputs.dir(localPropertiesDir)

    doLast {
        val packageName = "$appId.config"
        val packageDir = file(path = "$localPropertiesDir/kotlin/${packageName.replace(oldChar = '.', newChar = '/')}")
        packageDir.mkdirs()

        val supabaseUrl = localProperties.getProperty("SUPABASE_URL")
        val supabaseKey = localProperties.getProperty("SUPABASE_KEY")

        file("$packageDir/Config.kt").writeText(
            """
            package $packageName

            internal object Config {
                const val SUPABASE_URL: String = "$supabaseUrl"
                const val SUPABASE_KEY: String = "$supabaseKey"
            }
            """.trimIndent()
        )
    }
}

tasks.withType<KotlinCompile>().configureEach {
    dependsOn("generateLocalProperties")
}

tasks.withType<Test> {
    jvmArgs("--enable-native-access=ALL-UNNAMED")
}

kotlin {
    val appleList = listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    )

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
            kotlin.srcDir(localPropertiesDir.map { it.asFile.resolve(relative = "kotlin") })
            dependencies {
                implementation(dependencyNotation = compose.runtime)
                implementation(dependencyNotation = compose.foundation)
                implementation(dependencyNotation = compose.material3)
                implementation(dependencyNotation = compose.ui)
                implementation(dependencyNotation = compose.components.resources)
                implementation(dependencyNotation = compose.components.uiToolingPreview)
                implementation(dependencyNotation = compose.materialIconsExtended)
                implementation(dependencyNotation = project.dependencies.platform(libs.supabase.bom))
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
                implementation(dependencyNotation = project.dependencies.platform(libs.firebase.bom))
                implementation(dependencyNotation = libs.bundles.android)
            }
        }

        val desktopMain by getting {
            dependencies {
                implementation(dependencyNotation = compose.desktop.currentOs)
                implementation(dependencyNotation = libs.bundles.desktop)
            }
        }

        val appleMain by creating {
            dependsOn(other = commonMain)
            dependencies {
                implementation(dependencyNotation = libs.bundles.ios)
            }
        }
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
        jvmArgs += "--enable-native-access=ALL-UNNAMED"

        nativeDistributions {
            packageName = "The Last Voyage"
            packageVersion = "1.0.0"
            description = "A Compose Multiplatform adventure."
            vendor = appId

            targetFormats(
                TargetFormat.Dmg,
                TargetFormat.Msi,
                TargetFormat.Deb
            )

            macOS {
                iconFile.set(project.file("src/commonMain/composeResources/drawable/ic_launcher_round.icns"))
            }
            windows {
                iconFile.set(project.file("src/commonMain/composeResources/drawable/ic_launcher_round.ico"))
            }
            linux {
                iconFile.set(project.file("src/commonMain/composeResources/drawable/ic_launcher_round.png"))
            }
        }
    }
}

sqldelight {
    databases {
        create("AppDatabase") {
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
                }
            }
        }
    }
}
