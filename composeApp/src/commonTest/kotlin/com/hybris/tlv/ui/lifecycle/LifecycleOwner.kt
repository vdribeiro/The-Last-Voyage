package com.hybris.tlv.ui.lifecycle

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry

internal val lifecycleOwner: LifecycleOwner
    get() = object: LifecycleOwner {
        override val lifecycle: LifecycleRegistry = LifecycleRegistry(provider = this)

        init {
            lifecycle.currentState = Lifecycle.State.RESUMED
        }
    }
