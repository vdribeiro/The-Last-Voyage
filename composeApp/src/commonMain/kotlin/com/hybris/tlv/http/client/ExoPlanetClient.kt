package com.hybris.tlv.http.client

import com.hybris.tlv.http.request.Request
import com.hybris.tlv.http.response.Response
import io.ktor.client.call.body
import io.ktor.client.request.request
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.contentType
import io.ktor.http.encodeURLPath
import io.ktor.util.flattenEntries
import io.ktor.client.HttpClient as KtorHttpClient

internal class ExoPlanetClient(
    private val httpClient: KtorHttpClient,
): HttpClient {

    @Throws(Throwable::class)
    override suspend fun <T> get(request: Request): Response<T> {
        val response = httpClient.request {
            with(receiver = request) {
                method = HttpMethod.Get
                url(path = path.encodeURLPath())
                contentType(type = ContentType.Application.Json)
                queryMap.forEach { url.encodedParameters.append(name = it.key, value = it.value) }
            }
        }

        val headers = response.headers.flattenEntries().toMap()
        @Suppress("UNCHECKED_CAST")
        val body = response.call.body<String>() as T
        return Response(headers = headers, body = body)
    }

    @Throws(Throwable::class)
    override suspend fun <T> post(request: Request): Response<T> = throw Throwable("This client is for GET requests only")

    @Throws(Throwable::class)
    override suspend fun <T> patch(request: Request): Response<T> = throw Throwable("This client is for GET requests only")

    @Throws(Throwable::class)
    override suspend fun <T> delete(request: Request): Response<T> = throw Throwable("This client is for GET requests only")
}
