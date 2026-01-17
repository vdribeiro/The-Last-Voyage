package com.hybris.tlv

import kotlin.test.Test
import kotlin.test.assertEquals
import androidx.compose.foundation.focusable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.click
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performKeyInput
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.requestFocus
import androidx.compose.ui.test.swipeDown
import androidx.compose.ui.test.swipeLeft
import androidx.compose.ui.test.swipeRight
import androidx.compose.ui.test.swipeUp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.hybris.tlv.audio.AudioPlayer
import com.hybris.tlv.cheats.enableGestureCheats
import com.hybris.tlv.cheats.konamiCode
import com.hybris.tlv.cheats.konamiGestureCode
import com.hybris.tlv.cheats.rememberKeySequenceCheats
import com.hybris.tlv.command.Command
import com.hybris.tlv.command.sendCommand
import com.hybris.tlv.flag.FeatureFlags
import com.hybris.tlv.navigation.Screen
import com.hybris.tlv.theme.modifier.Gesture

@OptIn(ExperimentalTestApi::class)
internal class AppTest: TestCase() {

    @Test
    fun command() = runUITest(mockNavigation = false) {
        FeatureFlags.set {
            it.copy(
                reset = false,
                http = false,
                archive = false,
                music = true
            )
        }
        lateinit var navController: NavHostController
        setScreen {
            navController = rememberNavController()
            App(
                navController = navController,
                config = config,
                useCases = useCases,
                audioPlayer = AudioPlayer()
            )
        }
        sendCommand(command = Command.ToggleAudio)
        assertEquals(expected = listOf(Screen.Splash).toStringList(), actual = navController.getScreens())
        sendCommand(command = Command.Navigate(screen = Screen.MainMenu))
        assertEquals(expected = listOf(Screen.Splash, Screen.MainMenu).toStringList(), actual = navController.getScreens())
        sendCommand(command = Command.Navigate(screen = Screen.Game(ship = ship)))
        assertEquals(expected = listOf(Screen.Splash, Screen.MainMenu, Screen.Game).toStringList(), actual = navController.getScreens())
        sendCommand(command = Command.Navigate(screen = Screen.MainMenu))
        assertEquals(expected = listOf(Screen.Splash, Screen.MainMenu).toStringList(), actual = navController.getScreens())
        sendCommand(command = Command.Back)
        assertEquals(expected = listOf(Screen.Splash).toStringList(), actual = navController.getScreens())
    }

    @Test
    fun cheats() = runUITest(mockNavigation = false) {
        FeatureFlags.set {
            it.copy(
                reset = false,
                http = false,
                archive = false
            )
        }
        lateinit var navController: NavHostController
        setScreen {
            navController = rememberNavController()
            App(
                modifier = Modifier
                    .testTag(tag = "app")
                    .focusRequester(focusRequester = FocusRequester()).focusable()
                    .onKeyEvent(onKeyEvent = rememberKeySequenceCheats())
                    .enableGestureCheats(),
                navController = navController,
                config = config,
                useCases = useCases,
                audioPlayer = AudioPlayer()
            )
        }

        onNodeWithTag(testTag = "app")
            .requestFocus()
            .performKeyInput {
                konamiCode.forEach { key ->
                    keyDown(key = key)
                    keyUp(key = key)
                }
            }
        assertEquals(expected = listOf(Screen.Splash, Screen.Cheat).toStringList(), actual = navController.getScreens())

        sendCommand(command = Command.Back)
        assertEquals(expected = listOf(Screen.Splash).toStringList(), actual = navController.getScreens())
        onNodeWithTag(testTag = "app")
            .performTouchInput {
                fun performGesture(direction: Gesture) {
                    val distance = 200f
                    when (direction) {
                        Gesture.SWIPE_UP -> swipeUp(startY = 500f, endY = 500f - distance)
                        Gesture.SWIPE_DOWN -> swipeDown(startY = 200f, endY = 200f + distance)
                        Gesture.SWIPE_LEFT -> swipeLeft(startX = 500f, endX = 500f - distance)
                        Gesture.SWIPE_RIGHT -> swipeRight(startX = 200f, endX = 200f + distance)
                        Gesture.TAP -> click()
                    }
                }
                konamiGestureCode.forEach { gesture ->
                    performGesture(direction = gesture)
                }
            }
        assertEquals(expected = listOf(Screen.Splash, Screen.Cheat).toStringList(), actual = navController.getScreens())
    }

    private fun <T> List<T>.toStringList(): List<String> = map {
        it.toString().substringAfter(delimiter = "$").substringBefore(delimiter = "$")
    }

    private fun NavHostController.getScreens(): List<String> {
        return currentBackStack.value
            .mapNotNull { it.destination.route }
            .map { it.substringAfterLast(delimiter = ".").substringBefore(delimiter = "?") }
            .filter { it.isNotBlank() }
    }
}
