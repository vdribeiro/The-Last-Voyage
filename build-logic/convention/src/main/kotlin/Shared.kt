import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.gradle.api.JavaVersion

val appId: String = "com.hybris.tlv"
val appName: String = "The Last Voyage"
val appVersion: String = "1.2.0"
val appVersionNumber: Long = 18


val jdkVersion = 21
val jvmVersion = JvmTarget.JVM_21
val javaVersion = JavaVersion.VERSION_21

val androidSdkTarget: IntRange = 26..36

val iosTarget: String = "16.0"
