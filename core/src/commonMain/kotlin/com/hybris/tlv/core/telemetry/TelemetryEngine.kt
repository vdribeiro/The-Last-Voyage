package com.hybris.tlv.core.telemetry

/**
 * Defines the standard capabilities for capturing diagnostic data, including structured logging, exception tracking, and user-originated feedback.
 * Implementations are responsible for determining how this data is persisted or transmitted based on platform-specific capabilities.
 */
interface TelemetryEngine {

    /**
     * Records an informational event.
     *
     * @param tag A string identifier used to categorize the event.
     * @param message The detailed information to be recorded.
     */
    fun info(tag: String, message: String)

    /**
     * Records a diagnostic error or exception.
     *
     * @param tag A string identifier for the source of the error.
     * @param message A human-readable description of the failure.
     * @param throwable An optional [Throwable] to provide stack trace and causality details.
     */
    fun error(tag: String, message: String, throwable: Throwable?)

    /**
     * Processes explicit user feedback.
     *
     * Unlike automated logs, feedback is typically treated as a high-value signal often routed to external support or product analytics tools.
     *
     * @param message The verbatim content provided by the user.
     */
    fun feedback(message: String)
}
