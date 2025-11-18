package com.hybris.tlv

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.hybris.tlv.lifecycle.LifecycleCoroutine
import com.hybris.tlv.lifecycle.Register
import com.hybris.tlv.media.AudioPlayer.Action
import com.hybris.tlv.media.getTracks
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.ui.navigation.NavigationState
import com.hybris.tlv.ui.navigation.ScreenBuilder
import com.hybris.tlv.ui.navigation.backNavigation
import com.hybris.tlv.ui.store.StoreFactory
import com.hybris.tlv.ui.theme.AppTheme

@Composable
internal fun App(dependency: Dependency) = AppTheme {
    val navController = rememberNavController()


    val config = dependency.config
    val useCases = dependency.useCases
    val audioPlayer = dependency.audioPlayer

    val navigation: NavigationManager = viewModel { NavigationManager(initialState = NavigationState()) }
    val storeFactory: StoreFactory = remember(key1 = navigation) {
        StoreFactory(
            navigation = navigation,
            audioPlayer = audioPlayer,
            config = config,
            useCases = useCases
        )
    }
    val screenBuilder: ScreenBuilder = remember(key1 = storeFactory) { ScreenBuilder(storeFactory = storeFactory) }

    val navigationState by navigation.stateFlow.collectAsState()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .enableGestureCheats(config = config)
            .backNavigation { navigation.back() }) {
        // Render Screen
        screenBuilder.Screen(navigationState = navigationState)
    }

    // Setup Audio Player
    LifecycleCoroutine(navigationState.route) {
        val playlist = getTracks(route = navigationState.route)
        if (playlist != null) audioPlayer.action(action = Action.Play(playlist = playlist))
    }
    Register(
        onBackground = { audioPlayer.action(action = Action.Pause) },
        onForeground = { audioPlayer.action(action = Action.Resume) },
    )
}
