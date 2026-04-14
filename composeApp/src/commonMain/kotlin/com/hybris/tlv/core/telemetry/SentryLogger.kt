package com.hybris.tlv.core.telemetry

import com.hybris.tlv.platform.Property
import com.hybris.tlv.test.ExcludeFromTesting
import io.sentry.kotlin.multiplatform.Sentry
import io.sentry.kotlin.multiplatform.SentryLevel
import io.sentry.kotlin.multiplatform.protocol.Breadcrumb
import io.sentry.kotlin.multiplatform.protocol.UserFeedback

/**
 * Sentry logger.
 * This object manages the lifecycle and event reporting for Sentry. It maps common logging calls to Sentry features:
 * - **Info:** calls become [Breadcrumb]s to provide context for future crashes.
 * - **Error:** calls become Captured Messages or Exceptions.
 * - **Feedback:** calls are linked to Sentry's User Feedback system.
 */
@ExcludeFromTesting
internal object SentryLogger {

    /**
     * Initializes Sentry.
     * Configures the DSN, sample rates, and dynamically generates a release name.
     */
    internal fun init() =
        Sentry.init { options ->
            options.dsn = Property.sentry
            options.sampleRate = 1.0
            options.tracesSampleRate = 0.5
            options.release = "${
                Property.APP_NAME
                    .lowercase()
                    .replace(regex = "\\s+".toRegex(), replacement = "")
            }@${Property.APP_VERSION}"
        }

    /**
     * Records a [Breadcrumb] in the current Sentry scope.
     * Breadcrumbs are not sent immediately but are attached to subsequent error reports to help reconstruct the state of the application leading up to a failure.
     *
     * @param tag Mapped to the Sentry "category" field.
     * @param message The informational text describing the event.
     */
    fun info(tag: String, message: String) =
        Sentry.addBreadcrumb(breadcrumb = Breadcrumb().apply {
            this.level = SentryLevel.INFO
            this.category = tag
            this.message = message
        })

    /**
     * Reports an issue to the Sentry server immediately.
     * If a [throwable] is provided, it is captured as a "Crash" or "Issue" with a full stack trace. Otherwise, a simple "Message" is captured instead.
     *
     * @param tag Injected into the Sentry event as a searchable Tag.
     * @param message The error description, added as "Extra" context if a throwable is present.
     * @param throwable The exception to report, if any.
     */
    fun error(tag: String, message: String, throwable: Throwable? = null) =
        when {
            throwable == null -> Sentry.captureMessage(message = message) { scope ->
                scope.setTag(key = "tag", value = tag)
            }

            else -> Sentry.captureException(throwable = throwable) { scope ->
                scope.setTag(key = "tag", value = tag)
                scope.setExtra(key = "message", value = message)
            }
        }

    /**
     * Submits a [UserFeedback] report to Sentry.
     * This creates a relationship between a newly captured message and the feedback comments provided by the user.
     *
     * @param message The user's feedback or comments.
     */
    fun feedback(message: String) =
        Sentry.captureUserFeedback(userFeedback = UserFeedback(sentryId = Sentry.captureMessage(message = message)).apply {
            this.comments = message
        })
}
