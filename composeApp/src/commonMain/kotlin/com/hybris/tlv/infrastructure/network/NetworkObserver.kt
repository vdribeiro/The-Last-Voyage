@file:ShadowedInTesting

package com.hybris.tlv.infrastructure.network

import kotlinx.coroutines.flow.Flow
import com.hybris.tlv.test.ShadowedInTesting

/**
 * Observe [NetworkStatus].
 */
internal expect fun observeNetworkStatus(): Flow<NetworkStatus>
