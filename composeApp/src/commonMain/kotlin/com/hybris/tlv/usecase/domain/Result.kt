package com.hybris.tlv.usecase.domain

internal sealed class Result<T> {
    data class Success<T>(val list: List<T>): Result<T>()
    data class PartialSuccess<T>(val list: List<T>, val total: Int): Result<T>()
    data class Error<T>(val error: String): Result<T>()
}