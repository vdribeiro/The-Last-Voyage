package com.hybris.tlv

import kotlin.test.Test
import kotlin.test.assertEquals
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.hybris.tlv.audio.AudioPlayer
import com.hybris.tlv.navigation.Screen

@OptIn(ExperimentalTestApi::class)
internal class AppTest: TestCase() {

    @Test
    fun navigate() = runUITest(mockNavigation = false) {
        setFlag {
            it.copy(
                http = false,
                reset = false,
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
        assertEquals(expected = listOf(Screen.Splash).toStringList(), actual = navController.getScreens())
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
