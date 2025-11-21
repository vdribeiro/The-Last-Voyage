package com.hybris.tlv

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.hybris.tlv.media.AudioPlayer
import com.hybris.tlv.ui.navigation.Navigation
import com.hybris.tlv.ui.navigation.toScreen
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

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val screen = remember(key1 = navBackStackEntry) { navBackStackEntry?.toScreen() }
    AudioPlayer(
        audioPlayer = dependency.audioPlayer,
        screen = screen
    )
}
