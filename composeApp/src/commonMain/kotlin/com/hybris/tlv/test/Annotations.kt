package com.hybris.tlv.test

/**
 * Indicates that this element should not be included in testing.
 */
@Retention(value = AnnotationRetention.BINARY)
@Target(
    AnnotationTarget.CLASS,
    AnnotationTarget.FUNCTION,
)
internal annotation class ExcludeFromTesting

/**
 * Indicates that this file must be shadowed in testing.
 */
@Retention(value = AnnotationRetention.BINARY)
@Target(AnnotationTarget.FILE)
internal annotation class ShadowedInTesting
