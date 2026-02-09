package com.hybris.tlv.data.http

import com.hybris.tlv.domain.flag.FeatureFlags.flags

internal fun isInternetAvailable(): Boolean = flags.http
