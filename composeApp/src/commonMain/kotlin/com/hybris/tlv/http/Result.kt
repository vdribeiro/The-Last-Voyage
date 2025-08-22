package com.hybris.tlv.http

internal sealed interface Result<T> {
    data class Success<T>(val list: List<T>): Result<T>
    data class Error<T>(val error: String): Result<T>
}
