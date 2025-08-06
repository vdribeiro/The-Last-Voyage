package com.hybris.tlv

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.hybris.tlv.media.ambientTracks
import com.hybris.tlv.media.gameOverTracks
import com.hybris.tlv.media.gameTracks
import com.hybris.tlv.media.rememberAudioPlayer
import com.hybris.tlv.ui.navigation.Navigation
import com.hybris.tlv.ui.navigation.Navigation.Screen
import com.hybris.tlv.ui.theme.AppTheme

@Composable
internal fun App(core: Core) {
    AppTheme {
        val mediaPlayer = rememberAudioPlayer()
        val navigation = remember { Navigation(core = core) }
        val navigationState by navigation.stateFlow.collectAsState()

        if (!navigationState.music) mediaPlayer.stop() else {
            when (navigationState.screen) {
                Screen.SPLASH,
                Screen.MAIN_MENU,
                Screen.NEW_GAME,
                Screen.EXPLORE,
                Screen.STELLAR_EXPLORER,
                Screen.SCORES,
                Screen.ACHIEVEMENTS,
                Screen.CREDITS -> mediaPlayer.play(*ambientTracks)

                Screen.GAME,
                Screen.EVENT -> mediaPlayer.play(*gameTracks)

                Screen.ERROR,
                Screen.GAME_OVER -> mediaPlayer.play(*gameOverTracks)
            }
        }

        when (navigationState.screen) {
            Screen.ERROR -> navigation.ErrorScreen(state = navigationState.state)
            Screen.SPLASH -> navigation.SplashScreen(state = navigationState.state)
            Screen.MAIN_MENU -> navigation.MainMenuScreen(state = navigationState.state)
            Screen.NEW_GAME -> navigation.NewGameScreen(state = navigationState.state)
            Screen.GAME -> navigation.GameScreen(state = navigationState.state)
            Screen.EVENT -> navigation.EventScreen(state = navigationState.state)
            Screen.GAME_OVER -> navigation.GameOverScreen(state = navigationState.state)
            Screen.EXPLORE -> navigation.ExploreScreen(state = navigationState.state)
            Screen.STELLAR_EXPLORER -> navigation.StellarExplorerScreen(state = navigationState.state)
            Screen.SCORES -> navigation.ScoreScreen(state = navigationState.state)
            Screen.ACHIEVEMENTS -> navigation.AchievementScreen(state = navigationState.state)
            Screen.CREDITS -> navigation.CreditsScreen(state = navigationState.state)
        }
    }
}
