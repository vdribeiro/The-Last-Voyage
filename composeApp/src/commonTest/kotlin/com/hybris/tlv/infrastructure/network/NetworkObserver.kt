@file:Suppress("unused")

package com.hybris.tlv.infrastructure.network

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import com.hybris.tlv.domain.flag.FeatureFlags.flags

internal fun observeNetworkStatus(): Flow<NetworkStatus> =
    flowOf(value = NetworkStatus(hasInternet = flags.http))
