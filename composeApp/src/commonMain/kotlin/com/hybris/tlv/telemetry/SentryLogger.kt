package com.hybris.tlv.telemetry

import io.sentry.kotlin.multiplatform.Sentry
import io.sentry.kotlin.multiplatform.SentryLevel
import io.sentry.kotlin.multiplatform.protocol.Breadcrumb
import io.sentry.kotlin.multiplatform.protocol.SdkVersion
import io.sentry.kotlin.multiplatform.protocol.User

/**
 * Sentry logger.
 */
internal object SentryLogger {


    /**
     * Initialize Sentry.
     */
    internal fun init() {
        // TODO
        //Sentry.init { options ->
        //    options.dsn = BuildConfig.SENTRY_DSN
        //    options.release = "TLV@${BuildConfig.VERSION_NAME}+${BuildConfig.VERSION_CODE}"
        //    options.debug = BuildConfig.DEBUG
        //    options.environment = if (BuildConfig.DEBUG) "development" else "production"
        //    options.sdk = SdkVersion()
        //    options.attachViewHierarchy = BuildConfig.DEBUG
        //    options.sampleRate = 1.0
        //    options.tracesSampleRate = 0.2
        //}
        //
        //val user = User().apply {
        //    id = "player123"
        //    username = "GamingGod"
        //    email = "player123@example.com"
        //}
        //Sentry.setUser(user)
    }

    /**
     * Logs an informational message as a breadcrumb in Sentry.
     * Breadcrumbs are used to record a trail of events that led up to an issue.
     */
    fun info(tag: String, message: String) {
        Sentry.addBreadcrumb(breadcrumb = Breadcrumb().apply {
            this.level = SentryLevel.INFO
            this.category = tag
            this.message = message

        })
    }

    /**
     * Logs an error message to Sentry.
     * If a [throwable] is provided, it captures the exception along with the [tag] and [message],
     * otherwise it captures the [message] with the [tag].
     */
    fun error(tag: String, message: String, throwable: Throwable? = null) {
        when {
            throwable == null -> Sentry.captureMessage(message) { scope -> scope.setTag("tag", tag) }
            else -> Sentry.captureException(throwable = throwable) { scope ->
                scope.setTag("tag", tag)
                scope.setExtra("message", message)
            }
        }
    }
}
