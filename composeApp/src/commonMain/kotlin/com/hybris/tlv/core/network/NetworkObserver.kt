@file:ShadowedInTesting

package com.hybris.tlv.core.network

import kotlinx.coroutines.flow.Flow
import com.hybris.tlv.test.ShadowedInTesting

/**
 * Observe [NetworkStatus].
 */
internal expect fun observeNetworkStatus(): Flow<NetworkStatus>
