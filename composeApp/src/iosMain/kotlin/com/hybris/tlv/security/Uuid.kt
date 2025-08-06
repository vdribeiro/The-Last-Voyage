package com.hybris.tlv.security

import platform.Foundation.NSUUID

internal actual fun generateUuid(): String =
    NSUUID().UUIDString()
