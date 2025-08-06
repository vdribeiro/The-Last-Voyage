package com.hybris.tlv.http.response

internal data class Response<T>(
    val headers: Map<String, String>,
    val body: T,
)
