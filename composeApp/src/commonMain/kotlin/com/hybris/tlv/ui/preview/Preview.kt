package com.hybris.tlv.ui.preview

import com.hybris.tlv.media.AudioPlayer
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.ui.navigation.NavigationState
import com.hybris.tlv.ui.store.Store

private val testNavigation by lazy {
    NavigationManager(
        initialState = NavigationState()
    )
}
private val testAudioPlayer by lazy { AudioPlayer() }

internal fun <State, Action> getStore(initialState: State): Store<State, Action> = Store(
    navigation = testNavigation,
    audioPlayer = testAudioPlayer,
    initialState = initialState
)
