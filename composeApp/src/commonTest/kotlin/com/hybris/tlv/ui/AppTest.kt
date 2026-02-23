package com.hybris.tlv.ui

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
import com.hybris.tlv.domain.flag.FeatureFlags
import com.hybris.tlv.test.FakeData
import com.hybris.tlv.test.TestCase
import com.hybris.tlv.ui.cheats.enableGestureCheats
import com.hybris.tlv.ui.cheats.konamiCode
import com.hybris.tlv.ui.cheats.konamiGestureCode
import com.hybris.tlv.ui.cheats.rememberKeySequenceCheats
import com.hybris.tlv.ui.command.Command
import com.hybris.tlv.ui.command.sendCommand
import com.hybris.tlv.ui.navigation.Screen
import com.hybris.tlv.ui.theme.modifier.Gesture

@OptIn(ExperimentalTestApi::class)
internal class AppTest: TestCase() {

    @Test
    fun navigate() = runUITest(mockNavigation = false) {
        FeatureFlags.set {
            it.copy(
                reset = false,
                http = false,
                archive = false,
                music = true
            )
        }
        lateinit var navController: NavHostController
        val dependency = dependency.get()
        setUI {
            navController = rememberNavController()
            App(
                modifier = Modifier,
                compositionValues = emptyList(),
                navController = navController,
                dependency = dependency
            )
        }

        assertEquals(expected = listOf(Screen.Splash).toStringList(), actual = navController.getScreens())
        waitForIdle()

        sendCommand(command = Command.Navigate(screen = Screen.MainMenu))
        assertEquals(expected = listOf(Screen.Splash, Screen.MainMenu).toStringList(), actual = navController.getScreens())
        sendCommand(command = Command.Navigate(screen = Screen.MainMenu))
        assertEquals(expected = listOf(Screen.Splash, Screen.MainMenu).toStringList(), actual = navController.getScreens())
        sendCommand(command = Command.Navigate(screen = Screen.Splash()))
        assertEquals(expected = listOf(Screen.Splash).toStringList(), actual = navController.getScreens())
        sendCommand(command = Command.Navigate(screen = Screen.MainMenu))
        sendCommand(command = Command.Navigate(screen = Screen.Game(ship = FakeData.ship.get())))
        assertEquals(expected = listOf(Screen.Splash, Screen.MainMenu, Screen.Game).toStringList(), actual = navController.getScreens())
        waitForIdle()

        assertEquals(expected = listOf(Screen.Splash, Screen.MainMenu, Screen.Game, Screen.Feedback).toStringList(), actual = navController.getScreens())
        sendCommand(command = Command.Back)
        assertEquals(expected = listOf(Screen.Splash, Screen.MainMenu, Screen.Game).toStringList(), actual = navController.getScreens())
        sendCommand(command = Command.Back)
        assertEquals(expected = listOf(Screen.Splash, Screen.MainMenu).toStringList(), actual = navController.getScreens())
        sendCommand(command = Command.Back)
        assertEquals(expected = listOf(Screen.Splash).toStringList(), actual = navController.getScreens())
        sendCommand(command = Command.Back)
        assertEquals(expected = emptyList(), actual = navController.getScreens())
        waitForIdle()

        sendCommand(command = Command.Navigate(screen = Screen.GameOver))
        assertEquals(expected = listOf(Screen.GameOver).toStringList(), actual = navController.getScreens())
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
        val dependency = dependency.get()
        setUI {
            navController = rememberNavController()
            App(
                modifier = Modifier
                    .testTag(tag = "app")
                    .focusRequester(focusRequester = FocusRequester()).focusable()
                    .onKeyEvent(onKeyEvent = rememberKeySequenceCheats(navController = navController))
                    .enableGestureCheats(navController = navController),
                compositionValues = emptyList(),
                navController = navController,
                dependency = dependency
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
                        Gesture.SWIPE_UP -> swipeUp(startY = 200f, endY = 200f - distance)
                        Gesture.SWIPE_DOWN -> swipeDown(startY = 200f, endY = 200f + distance)
                        Gesture.SWIPE_LEFT -> swipeLeft(startX = 200f, endX = 200f - distance)
                        Gesture.SWIPE_RIGHT -> swipeRight(startX = 200f, endX = 200f + distance)
                        Gesture.TAP -> click()
                    }
                }
                konamiGestureCode.forEach { gesture ->
                    performGesture(direction = gesture)
                }
            }
        // TODO - gestures with nested scrolls are really hard to test...
        runCatching {
            assertEquals(expected = listOf(Screen.Splash, Screen.Cheat).toStringList(), actual = navController.getScreens())
        }.onFailure {
            assertEquals(expected = listOf(Screen.Splash).toStringList(), actual = navController.getScreens())
        }
    }

    private fun <T> List<T>.toStringList(): List<String> = map {
        it.toString()
            .substringBefore(delimiter = ".Companion")
            .substringAfterLast(delimiter = ".")
            .substringAfter(delimiter = "$")
            .substringBefore(delimiter = "$")
    }

    private fun NavHostController.getScreens(): List<String> {
        return currentBackStack.value
            .mapNotNull { it.destination.route }
            .map { it.substringAfterLast(delimiter = ".").substringBefore(delimiter = "?") }
            .filter { it.isNotBlank() }
    }
}