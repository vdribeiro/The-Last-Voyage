plugins {
    alias(notation = libs.plugins.kotlin.multiplatform) apply false
    alias(notation = libs.plugins.cocoapods) apply false
    alias(notation = libs.plugins.kover) apply false
    alias(notation = libs.plugins.sentry) apply false
    alias(notation = libs.plugins.android.application) apply false
    alias(notation = libs.plugins.android.library) apply false
    alias(notation = libs.plugins.compose.multiplatform) apply false
    alias(notation = libs.plugins.compose.compiler) apply false
    alias(notation = libs.plugins.sqldelight) apply false
}
