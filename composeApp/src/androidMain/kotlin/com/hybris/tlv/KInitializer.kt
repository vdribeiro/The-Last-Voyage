package com.hybris.tlv

import android.content.Context
import android.content.ContextWrapper
import androidx.startup.Initializer

internal var applicationContext: Context = ContextWrapper(null)

class KInitializer: Initializer<Unit> {
    override fun create(context: Context) {
        applicationContext = context.applicationContext
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}
