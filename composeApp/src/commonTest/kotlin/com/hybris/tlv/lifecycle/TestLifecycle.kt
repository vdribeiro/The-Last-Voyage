package com.hybris.tlv.lifecycle

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry

internal class TestLifecycle: LifecycleOwner {
    override val lifecycle: LifecycleRegistry = LifecycleRegistry(provider = this)

    init {
        lifecycle.currentState = Lifecycle.State.RESUMED
    }
}
