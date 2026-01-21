package com.hybris.tlv.domain.flag

/**
 * Defines the application control flags.
 */
internal data class Flags(
    /**
     * Flag to enable or disable development mode.
     * This should be set to false for production builds.
     */
    val devMode: Boolean,
    /**
     * Flag to enable or disable a full data reset before syncing data.
     * This should be set to false for production builds.
     */
    val reset: Boolean,
    /**
     * Flag to enable or disable HTTP client. When false, network calls will fail.
     * This should be set to true for production builds.
     */
    val http: Boolean,
    /**
     * Flag to enable or disable network quality check. When false, the check is skipped.
     * This should be set to true for production builds.
     */
    val networkQuality: Boolean,
    /**
     * Flag to enable or disable fetching exoplanet data directly from the NASA archive when syncing data.
     * This should be set to false for production builds.
     */
    val archive: Boolean,
    /**
     * Flag to enable or disable the audio player in the application.
     * This should be set to true for production builds.
     */
    val music: Boolean
)
