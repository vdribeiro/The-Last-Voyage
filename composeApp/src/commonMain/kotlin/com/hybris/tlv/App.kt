package com.hybris.tlv

import kotlinx.serialization.Serializable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.hybris.tlv.lifecycle.LifecycleCoroutine
import com.hybris.tlv.lifecycle.Register
import com.hybris.tlv.media.AudioPlayer.Action
import com.hybris.tlv.media.getTracks
import com.hybris.tlv.ui.navigation.Screen
import com.hybris.tlv.ui.navigation.backNavigation
import com.hybris.tlv.ui.navigation.eventGraph
import com.hybris.tlv.ui.theme.AppTheme

@Composable
internal fun App(dependency: Dependency) = AppTheme {
    val config = dependency.config
    val audioPlayer = dependency.audioPlayer

    // Setup Navigation
    val navController = rememberNavController()
    NavHost(
        modifier = Modifier
            .fillMaxSize()
            .enableGestureCheats(config = config)
            .backNavigation { navController.popBackStack() },
        navController = navController,
        startDestination = Screen.Splash
    ) {
        eventGraph()
    }


    // Setup Audio Player
//    LifecycleCoroutine(navController.currentDestination) {
//        val playlist = getTracks(screen = navController.currentDestination)
//        if (playlist != null) audioPlayer.action(action = Action.Play(playlist = playlist))
//    }
//    Register(
//        onBackground = { audioPlayer.action(action = Action.Pause) },
//        onForeground = { audioPlayer.action(action = Action.Resume) },
//    )
}
