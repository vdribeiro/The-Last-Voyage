package com.hybris.tlv.data.http

/**
 * Result of a network request.
 *
 * @param T The type of the domain expected in a successful response.
 */
internal sealed interface Result<T> {

    /**
     * Represents a successful operation where data was retrieved and decoded.
     *
     * @param T The type of the items in the list.
     * @property list The collection of results returned by the network request.
     */
    data class Success<T>(val list: List<T>): Result<T>

    /**
     * Represents a failed operation due to networking issues, parsing errors, or application-level logic failures.
     *
     * @property error The [Throwable] containing the details of the failure.
     */
    data class Error<T>(val error: Throwable): Result<T>
}
