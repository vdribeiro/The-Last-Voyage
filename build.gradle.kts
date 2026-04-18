plugins {
    alias(notation = libs.plugins.kotlin.multiplatform) apply false
    alias(notation = libs.plugins.cocoapods) apply false
    alias(notation = libs.plugins.kover) apply false
    alias(notation = libs.plugins.sentry) apply false
    alias(notation = libs.plugins.android.application) apply false
    alias(notation = libs.plugins.android.library) apply false
    alias(notation = libs.plugins.android.kotlin.multiplatform.library) apply false
    alias(notation = libs.plugins.android.lint) apply false
    alias(notation = libs.plugins.compose.multiplatform) apply false
    alias(notation = libs.plugins.compose.compiler) apply false
    alias(notation = libs.plugins.compose.hotreload) apply false
    alias(notation = libs.plugins.sqldelight) apply false
    alias(notation = libs.plugins.ktor) apply false
    alias(notation = libs.plugins.kotlin.jvm) apply false
}
