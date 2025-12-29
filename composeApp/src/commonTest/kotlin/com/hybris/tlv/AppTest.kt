package com.hybris.tlv

import kotlin.test.Test
import androidx.compose.ui.test.ExperimentalTestApi
import com.hybris.tlv.audio.AudioPlayer
import com.hybris.tlv.navigation.Screen

@OptIn(ExperimentalTestApi::class)
internal class AppTest: TestCase() {

    @Test
    fun navigate() = runUITest(mockNavigation = false) {
        setScreen {
            App(
                config = config,
                useCases = useCases,
                audioPlayer = AudioPlayer()
            )
        }
        navigate(screen = Screen.Splash())
        assertNavigation(list = listOf(Screen.Splash()))
    }
}
