package com.hybris.tlv.ui.navigation

import androidx.navigationevent.NavigationEventDispatcher
import androidx.navigationevent.NavigationEventDispatcherOwner

internal val navigationEventDispatcherOwner: NavigationEventDispatcherOwner
    get() = object: NavigationEventDispatcherOwner {
        override val navigationEventDispatcher: NavigationEventDispatcher = NavigationEventDispatcher()
    }