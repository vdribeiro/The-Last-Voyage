import java.util.Properties
import kotlin.experimental.xor
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig
import org.gradle.internal.os.OperatingSystem

plugins {
    alias(notation = libs.plugins.kotlin.multiplatform)
    alias(notation = libs.plugins.kotlin.serialization)
    alias(notation = libs.plugins.cocoapods)
    alias(notation = libs.plugins.kover)
    alias(notation = libs.plugins.sentry)
    alias(notation = libs.plugins.android.application)
    alias(notation = libs.plugins.compose.multiplatform)
    alias(notation = libs.plugins.compose.compiler)
    alias(notation = libs.plugins.sqldelight)
}

//region Properties
val localProperties: Properties = Properties().apply {
    runCatching { rootProject.file("local.properties").takeIf { it.exists() }?.inputStream()?.use(block = this::load) }.getOrNull()
}
val appId: String = "com.hybris.tlv"
val appName: String = "The Last Voyage"
val appDescription: String = "An Educational Space Adventure"
val appFramework = "TLV"
val appVendor: String = "Hybris"
val appFolder = "/${appName.replace(oldValue = " ", newValue = "-")}/"
val appHomepage: String = "https://mammoth-gallium-e97.notion.site/The-Last-Voyage-2420fa355a5080da91ffd9262f430feb"
val appVersion: String = "1.2.0"
val appVersionNumber: Long = 18

val jdkVersion = 21
val jvmVersion = JvmTarget.JVM_21
val javaVersion = JavaVersion.VERSION_21

val androidSdkTarget: IntRange = 26..36
val androidKeyAlias: String = localProperties.getProperty("android.keyAlias", "")
val androidKeyPassword: String = localProperties.getProperty("android.keyPassword", "")
val androidStoreFile: File? = runCatching { rootProject.file(localProperties.getProperty("android.storeFile", "")) }.getOrNull()
val androidStorePassword: String = localProperties.getProperty("android.storePassword", "")

val iosTarget: String = "16.0"
val appleIdentity: String = localProperties.getProperty("mac.sign.identity", "")
val appleTeamId: String = localProperties.getProperty("mac.notarization.teamId", "")
val appleId: String = localProperties.getProperty("mac.notarization.appleId", "")
val applePassword: String = localProperties.getProperty("mac.notarization.password", "")
val appleLauncher: File get() = project.file("src/commonMain/composeResources/drawable/ic_launcher_apple.icns")

val windowsId = "580991aa-c884-4661-9876-5f36272fd26b"
val windowsLauncher: File get() = project.file("src/commonMain/composeResources/drawable/ic_launcher_win.ico")

val sentryDsn: String = localProperties.getProperty("sentryDsn", "")
val isRelease: Boolean
    get() = project.gradle.startParameter.taskNames.any {
        it.contains(other = "package", ignoreCase = true) || it.contains(other = "notarize", ignoreCase = true)
    }
val launcher: File get() = project.file("src/commonMain/composeResources/drawable/ic_launcher_round.png")
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

fun MinimalExternalModuleDependency.setClassifier() {
    artifact { this.classifier = osClassifier }
}

fun KotlinDependencyHandler.addJavaFx() = javafxDependencies.forEach {
    implementation(dependency = it.get()) { setClassifier() }
}

fun DependencyHandler.addJavaFx() = javafxDependencies.forEach {
    add(configuration = "javafx", dependency = it.get()) { setClassifier() }
}

// Module paths have to run after, otherwise it breaks the configuration
project.afterEvaluate {
    if (!isRelease) {
        compose.desktop.application.jvmArgs += "--module-path=$javafxModulePath"
        compose.desktop.application.jvmArgs += "--add-modules=$javafxModules"
    }
}
//endregion

//region Web
fun KotlinDependencyHandler.devNpm(library: Provider<MinimalExternalModuleDependency>): Dependency {
    val dependency = library.get()
    return devNpm(name = dependency.module.name, version = dependency.versionConstraint.displayName)
}

fun KotlinDependencyHandler.npm(library: Provider<MinimalExternalModuleDependency>): Dependency {
    val dependency = library.get()
    return npm(name = dependency.module.name, version = dependency.versionConstraint.displayName)
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
            kotlin.srcDir(generatePropertiesTask.map { it.outputs.files })
            dependencies {
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

        getByName("androidUnitTest") {
            dependencies {
                implementation(dependencyNotation = libs.bundles.android.test)
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

        val appleTest by creating {
            dependsOn(other = commonTest)
        }
        iosTargets.forEach { iosTarget ->
            sourceSets.getByName("${iosTarget.name}Test").dependsOn(other = appleTest)
        }

        getByName("desktopMain") {
            dependencies {
                implementation(dependencyNotation = compose.desktop.currentOs)
                implementation(dependencyNotation = libs.bundles.desktop)
                addJavaFx()
            }
        }

        getByName("desktopTest") {
            dependencies {
                implementation(dependencyNotation = libs.compose.test.junit)
            }
        }

        val webMain by creating {
            dependsOn(commonMain)
            dependencies {
                implementation(dependencyNotation = libs.bundles.web)
                implementation(dependencyNotation = npm(library = libs.sql.worker))
                implementation(dependencyNotation = npm(library = libs.sql.js))
                implementation(dependencyNotation = devNpm(library = libs.webpack))
            }
        }
        sourceSets.getByName("${webTarget.name}Main").dependsOn(other = webMain)

        val webTest by creating {
            dependsOn(commonTest)
        }
        sourceSets.getByName("${webTarget.name}Test").dependsOn(other = webTest)
    }

    cocoapods {
        version = appVersion
        summary = appDescription
        homepage = appHomepage
        ios.deploymentTarget = iosTarget
        podfile = project.file("../iosApp/Podfile")
    }
}

dependencies {
    debugImplementation(dependencyNotation = libs.compose.tooling)
    debugImplementation(dependencyNotation = libs.androidx.test.manifest)
    addJavaFx()
}

android {
    namespace = appId
    compileSdk = androidSdkTarget.last

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
        minSdk = androidSdkTarget.first
        targetSdk = androidSdkTarget.last
        versionCode = appVersionNumber.toInt()
        versionName = appVersion
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildFeatures {
        buildConfig = true
        resValues = true
    }
    sourceSets {
        getByName("main") {
            assets.directories.add("src/commonMain/resources")
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
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
    }
    testOptions {
        unitTests.isIncludeAndroidResources = true
    }
}

compose.desktop {
    application {
        mainClass = "$appId.MainKt"
        javaHome = System.getenv("JAVA_HOME").orEmpty()

        jvmArgs += listOf(
            "-Ddebug=${!isRelease}",
            "--enable-native-access=ALL-UNNAMED",
            "--enable-native-access=javafx.graphics",
            "--enable-native-access=javafx.media"
        )
        if (currentOS.isMacOsX) {
            jvmArgs += listOf(
                "-Xdock:icon=${appleLauncher.absolutePath}",
                "-Xdock:name=$appName",
                "-Dapple.awt.application.name=$appName"
            )
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
                bundleID = appId
                iconFile.set(appleLauncher)
                if (isRelease) {
                    entitlementsFile.set(project.file("src/desktopMain/resources/entitlements.plist"))
                    signing {
                        sign.set(true)
                        identity.set(appleIdentity)
                    }

                    notarization {
                        teamID.set(appleTeamId)
                        appleID.set(appleId)
                        password.set(applePassword)
                    }
                }
            }

            windows {
                upgradeUuid = windowsId
                iconFile.set(windowsLauncher)
                shortcut = true
                menu = true
                menuGroup = appVendor
            }

            linux {
                appCategory = "Game"
                iconFile.set(launcher)
                shortcut = true
            }
        }
    }
}

sqldelight {
    databases {
        create(name = "AppDatabase") {
            packageName.set("database")
            generateAsync.set(true)
            schemaOutputDirectory.set(file(path = "${project.projectDir}/src/commonMain/sqldelight/schema"))
        }
    }
}

kover {
    reports {
        filters {
            excludes {
                annotatedBy(
                    "kotlinx.serialization.Serializable",
                    "androidx.compose.ui.tooling.preview.Preview",
                    "com.hybris.tlv.test.ExcludeFromTesting",
                )
                packages("*.generated.*")
                classes(
                    "**ComposableSingletons**",
                    "**$**",
                    "database.Get*"
                )
            }
        }
        total {
            html { onCheck = true }
            log { onCheck = true }
            verify {
                onCheck = true
                rule {
                    bound { minValue = 90 }
                }
            }
        }
    }
}

// Enable native access for tests
tasks.withType<Test> {
    jvmArgs("--enable-native-access=ALL-UNNAMED")
}

tasks.register<Sync>("deployWeb") {
    group = "deployment"

    val distributionTask = tasks.named("wasmJsBrowserDistribution")
    val destinationDir = rootProject.layout.projectDirectory.dir("docs").asFile

    from(distributionTask)
    into(destinationDir)

    val head = "<head>\n    <base href=\"$appFolder\">"
    filesMatching("index.html") {
        filter { line ->
            if (line.contains("<head>")) line.replace("<head>", head) else line
        }
    }

    filesMatching("sqljs.worker.js") {
        filter { line ->
            line.replace("'sql.js'", "'./sql-wasm.js'")
                .replace("\"sql.js\"", "\"./sql-wasm.js\"")
                .replace("from 'sql.js'", "from './sql-wasm.js'")
        }
    }

    doLast {
        File(destinationDir, ".nojekyll").createNewFile()
    }
}

tasks.register("testAllAndReport") {
    group = "verification"
    description = "Runs all platform tests and generates a unified Kover coverage report."

    dependsOn("allTests")
    finalizedBy("koverHtmlReport")
}
