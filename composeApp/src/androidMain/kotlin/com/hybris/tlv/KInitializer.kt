package com.hybris.tlv

import android.content.Context
import androidx.startup.Initializer

internal lateinit var applicationContext: Context
    private set

class KInitializer: Initializer<Unit> {
    override fun create(context: Context) {
        applicationContext = context.applicationContext
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}
