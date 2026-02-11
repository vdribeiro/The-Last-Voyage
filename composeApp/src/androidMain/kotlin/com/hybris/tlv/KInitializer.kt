package com.hybris.tlv

import android.content.Context
import android.content.ContextWrapper
import androidx.startup.Initializer
import com.hybris.tlv.test.ExcludeFromTesting

internal var applicationContext: Context = ContextWrapper(null)

@ExcludeFromTesting
class KInitializer: Initializer<Unit> {
    override fun create(context: Context) {
        applicationContext = context.applicationContext
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}
