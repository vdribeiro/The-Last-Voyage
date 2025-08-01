package com.hybris.tlv.security

/**
 * Get a universally unique identifier (UUID) using the best available algorithm to prevent collisions.
 */
internal expect fun generateUuid(): String
