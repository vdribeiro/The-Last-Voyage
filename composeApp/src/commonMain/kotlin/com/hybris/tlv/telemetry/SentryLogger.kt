package com.hybris.tlv.telemetry

import com.hybris.tlv.platform.Property
import com.hybris.tlv.platform.isDebug
import io.sentry.kotlin.multiplatform.Sentry
import io.sentry.kotlin.multiplatform.SentryLevel
import io.sentry.kotlin.multiplatform.protocol.Breadcrumb
import io.sentry.kotlin.multiplatform.protocol.UserFeedback

/**
 * Sentry logger.
 */
internal object SentryLogger {

    private val available = Property.SENTRY_DSN.isNotBlank()

    /**
     * Initialize Sentry.
     */
    internal fun init() {
        if (!available) return
        Sentry.init { options ->
            options.dsn = Property.SENTRY_DSN
            options.release = "${Property.APP_NAME.lowercase().replace(regex = "\\s+".toRegex(), replacement = "")}@${Property.APP_VERSION}"

            options.debug = isDebug
            options.attachViewHierarchy = isDebug
            options.environment = if (isDebug) "dev" else "prod"

            options.sampleRate = 1.0
            options.tracesSampleRate = 0.5
        }
    }

    /**
     * Logs an informational message as a breadcrumb in Sentry.
     * Breadcrumbs are used to record a trail of events that led up to an issue.
     */
    fun info(tag: String, message: String) {
        if (!available) return
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
        if (!available) return
        when {
            throwable == null -> Sentry.captureMessage(message = message) { scope -> scope.setTag(key = "tag", value = tag) }
            else -> Sentry.captureException(throwable = throwable) { scope ->
                scope.setTag(key = "tag", value = tag)
                scope.setExtra(key = "message", value = message)
            }
        }
    }

    /**
     * Logs a user feedback to Sentry.
     */
    fun feedback(message: String) {
        if (!available) return
        Sentry.captureUserFeedback(userFeedback = UserFeedback(sentryId = Sentry.captureMessage(message = message)).apply {
            this.comments = message
        })
    }
}
