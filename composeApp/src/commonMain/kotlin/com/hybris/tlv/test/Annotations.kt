package com.hybris.tlv.test

/**
 * Indicates that this target will not be included in testing.
 */
@Retention(value = AnnotationRetention.BINARY)
@Target(AnnotationTarget.FILE, AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
internal annotation class ExcludeFromTesting

/**
 * Indicates that this target must be shadowed in testing, therefore it will also not be included in testing.
 */
@Retention(value = AnnotationRetention.BINARY)
@Target(AnnotationTarget.FILE, AnnotationTarget.CLASS)
internal annotation class ShadowedInTesting

/**
 * Indicates that this target has its visibility relaxed so it is accessible in tests.
 */
@Retention(value = AnnotationRetention.BINARY)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY)
internal annotation class VisibleOnlyForTesting
