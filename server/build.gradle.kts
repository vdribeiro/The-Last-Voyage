plugins {
    alias(notation = libs.plugins.ktor)
    alias(notation = libs.plugins.kotlin.jvm)
    application
}

group = "com.hybris.tlv"
version = "1.0.0"
application {
    mainClass.set("com.hybris.tlv.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

dependencies {
//    implementation(projects.shared)
    implementation(dependencyNotation = libs.bundles.server)
}