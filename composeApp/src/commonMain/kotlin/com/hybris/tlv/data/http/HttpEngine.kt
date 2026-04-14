package com.hybris.tlv.data.http

import io.ktor.client.engine.HttpClientEngine
import com.hybris.tlv.test.ExcludeFromTesting

/**
 * Factory function to instantiate a platform-specific [HttpClientEngine].
 *
 * @return A [HttpClientEngine] configured for the current platform and ready for the [HttpClientFactory].
 */
@ExcludeFromTesting
internal expect fun createHttpEngine(): HttpClientEngine
