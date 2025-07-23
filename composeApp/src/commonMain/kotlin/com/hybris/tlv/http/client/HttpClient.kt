package com.hybris.tlv.http.client

import com.hybris.tlv.http.request.Request
import com.hybris.tlv.http.response.Response

internal interface HttpClient {

    @Throws(Throwable::class)
    suspend fun <T> get(request: Request): Response<T>

    @Throws(Throwable::class)
    suspend fun <T> post(request: Request): Response<T>

    @Throws(Throwable::class)
    suspend fun <T> patch(request: Request): Response<T>

    @Throws(Throwable::class)
    suspend fun <T> delete(request: Request): Response<T>
}
