package com.hybris.tlv.test

/**
 * Indicates that this file should not be included in testing.
 */
@Retention(value = AnnotationRetention.BINARY)
@Target(AnnotationTarget.FILE, AnnotationTarget.CLASS)
internal annotation class ExcludeFromTesting

/**
 * Indicates that this file must be shadowed in testing, therefore it will also not be included in testing.
 */
@Retention(value = AnnotationRetention.BINARY)
@Target(AnnotationTarget.FILE, AnnotationTarget.CLASS)
internal annotation class ShadowedInTesting
