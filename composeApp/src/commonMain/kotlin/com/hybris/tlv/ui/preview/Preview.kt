package com.hybris.tlv.ui.preview

import com.hybris.tlv.flow.Dispatchers
import com.hybris.tlv.media.AudioPlayer
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.ui.navigation.NavigationState
import com.hybris.tlv.ui.store.Store

private val testDispatchers by lazy { Dispatchers() }
private val testNavigation by lazy {
    NavigationManager(
        dispatcher = testDispatchers,
        initialState = NavigationState()
    )
}
private val testAudioPlayer by lazy { AudioPlayer() }

internal fun <State, Action> getStore(initialState: State): Store<State, Action> = Store(
    dispatcher = testDispatchers,
    navigation = testNavigation,
    audioPlayer = testAudioPlayer,
    initialState = initialState
)
