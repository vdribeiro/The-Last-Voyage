plugins {
    id(id = "shared")
    alias(notation = libs.plugins.ktor)
    alias(notation = libs.plugins.kotlin.jvm)
    application
}

version = "1.0.0"
application {
    mainClass.set("$appId.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

dependencies {
    implementation(dependencyNotation = projects.shared)
    implementation(dependencyNotation = libs.bundles.server)
}