package com.hybris.tlv.data.http

import io.ktor.client.engine.HttpClientEngine

/**
 * Creates the http engine.
 */
internal expect fun createHttpEngine(): HttpClientEngine
