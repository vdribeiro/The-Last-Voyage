package com.hybris.tlv

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.hybris.tlv.media.AudioPlayer
import com.hybris.tlv.ui.navigation.Navigation
import com.hybris.tlv.ui.navigation.Screen
import com.hybris.tlv.ui.theme.AppTheme

@Composable
internal fun App(dependency: Dependency) = AppTheme {
    val navController = rememberNavController()
    Navigation(
        modifier = Modifier.enableGestureCheats(config = dependency.config),
        navController = navController,
        config = dependency.config,
        useCases = dependency.useCases,
    )

    val screen = navController.currentBackStackEntry?.toRoute<Screen>()
    AudioPlayer(
        audioPlayer = dependency.audioPlayer,
        screen = screen
    )
}
