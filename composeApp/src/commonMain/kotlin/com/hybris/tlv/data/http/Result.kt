package com.hybris.tlv.data.http

/**
 * Result of a network request.
 */
internal sealed interface Result<T> {
    data class Success<T>(val list: List<T>): Result<T>
    data class Error<T>(val error: Throwable): Result<T>
}
