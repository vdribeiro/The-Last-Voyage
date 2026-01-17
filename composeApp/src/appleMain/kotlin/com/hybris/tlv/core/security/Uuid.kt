package com.hybris.tlv.core.security

import platform.Foundation.NSUUID

internal actual fun generateUuid(): String =
    NSUUID().UUIDString()
