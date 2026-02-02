@file:ShadowedInTesting

package com.hybris.tlv.infrastructure.platform

import com.hybris.tlv.BuildConfig
import com.hybris.tlv.test.ShadowedInTesting

internal actual val isDebug: Boolean = BuildConfig.DEBUG

internal actual val platform: Platform = Platform.Android
