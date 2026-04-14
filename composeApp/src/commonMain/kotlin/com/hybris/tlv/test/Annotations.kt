package com.hybris.tlv.test

/**
 * Marks a target to be ignored by automated testing suites and coverage reports.
 *
 * This is primarily used for:
 * - **Platform-Specific Factories:** Code that instantiates native drivers which cannot be executed in a common unit test environment.
 * - **Manual Debug Tools:** UI components or logic used strictly for developer diagnostics that do not require verification.
 * - **Boilerplate Bridge Code:** Native code that acts purely as a passthrough to platform APIs.
 */
@Retention(value = AnnotationRetention.BINARY)
@Target(AnnotationTarget.FILE, AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
internal annotation class ExcludeFromTesting

/**
 * Signals that a function or property's visibility has been expanded (e.g. from `private` to `internal`) solely to allow access for unit tests.
 *
 * ### Usage Guidelines:
 * - This annotation should be applied when a member's logic is complex enough to require testing, but it shouldn't be part of the public API for the rest of the module.
 * - Developers should avoid calling members marked with this annotation in production code outside the owner class's scope.
 */
@Retention(value = AnnotationRetention.BINARY)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY)
internal annotation class VisibleForTesting
