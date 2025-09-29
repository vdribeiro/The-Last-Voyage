package com.hybris.tlv.telemetry

import io.sentry.kotlin.multiplatform.Sentry
import io.sentry.kotlin.multiplatform.SentryLevel
import io.sentry.kotlin.multiplatform.protocol.Breadcrumb

internal object SentryLogger {
    fun info(tag: String, message: String) {
        Sentry.addBreadcrumb(breadcrumb = Breadcrumb().apply {
            this.level = SentryLevel.INFO
            this.category = tag
            this.message = message

        })
    }

    fun error(tag: String, message: String, throwable: Throwable? = null) {
        when {
            throwable != null -> Sentry.captureException(throwable = throwable) { scope ->
                scope.setTag("tag", tag)
                scope.setExtra("message", message)
            }

            else -> Sentry.captureMessage(message) { scope ->
                scope.setTag("tag", tag)
            }
        }
    }
}
