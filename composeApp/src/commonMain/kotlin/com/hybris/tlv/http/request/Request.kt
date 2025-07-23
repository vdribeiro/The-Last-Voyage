package com.hybris.tlv.http.request

internal data class Request(
    val path: String,
    val queryMap: QueryMap = QueryMap(),
)
