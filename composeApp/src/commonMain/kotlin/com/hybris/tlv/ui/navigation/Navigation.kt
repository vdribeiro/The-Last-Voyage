package com.hybris.tlv.ui.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigationevent.NavigationEventInfo
import androidx.navigationevent.compose.NavigationBackHandler
import androidx.navigationevent.compose.rememberNavigationEventState
import com.hybris.tlv.domain.flag.FeatureFlags.flags
import com.hybris.tlv.ui.navigation.graph.achievementScreen
import com.hybris.tlv.ui.navigation.graph.catastropheExplorerScreen
import com.hybris.tlv.ui.navigation.graph.catastropheScreen
import com.hybris.tlv.ui.navigation.graph.cheatScreen
import com.hybris.tlv.ui.navigation.graph.creditScreen
import com.hybris.tlv.ui.navigation.graph.eventExplorerScreen
import com.hybris.tlv.ui.navigation.graph.eventScreen
import com.hybris.tlv.ui.navigation.graph.feedbackScreen
import com.hybris.tlv.ui.navigation.graph.gameOverScreen
import com.hybris.tlv.ui.navigation.graph.gameScreen
import com.hybris.tlv.ui.navigation.graph.helpScreen
import com.hybris.tlv.ui.navigation.graph.mainMenuScreen
import com.hybris.tlv.ui.navigation.graph.newGameScreen
import com.hybris.tlv.ui.navigation.graph.scoreScreen
import com.hybris.tlv.ui.navigation.graph.splashScreen
import com.hybris.tlv.ui.navigation.graph.stellarExplorerScreen
import com.hybris.tlv.ui.navigation.graph.tutorialScreen
import com.hybris.tlv.ui.screen.StoreFactory

internal val LocalNavController = staticCompositionLocalOf<NavHostController?> { null }

/**
 * The main navigation host for the application.
 * This composable sets up the [NavHost] and defines all the possible navigation destinations within the app,
 * linking each [Screen] to its corresponding composable content.
 */
@Composable
internal fun Navigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    storeFactory: StoreFactory
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Screen.Splash(reset = flags.reset),
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None }
    ) {
        splashScreen(storeFactory = storeFactory)
        cheatScreen(storeFactory = storeFactory)
        mainMenuScreen(storeFactory = storeFactory)
        helpScreen(storeFactory = storeFactory)
        feedbackScreen(storeFactory = storeFactory)
        newGameScreen(storeFactory = storeFactory)
        catastropheScreen(storeFactory = storeFactory)
        tutorialScreen(storeFactory = storeFactory)
        gameScreen(storeFactory = storeFactory)
        eventScreen(storeFactory = storeFactory)
        gameOverScreen(storeFactory = storeFactory)
        stellarExplorerScreen(storeFactory = storeFactory)
        scoreScreen(storeFactory = storeFactory)
        achievementScreen(storeFactory = storeFactory)
        creditScreen(storeFactory = storeFactory)
        catastropheExplorerScreen(storeFactory = storeFactory)
        eventExplorerScreen(storeFactory = storeFactory)
    }

    LaunchedEffect(key1 = Unit) {
        receiveCommand { command ->
            when (command) {
                is Navigate.To -> navController.navigate(screen = command.screen)
                Navigate.Back -> navController.back()
            }
        }
    }
}

private object ScreenBackInfo: NavigationEventInfo()
/**
 * Composable for managing system back events like when a physical back button is pressed or a back gesture is completed.
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun NavigationHandler(onBack: (() -> Unit)?) {
    val navState = rememberNavigationEventState(currentInfo = ScreenBackInfo)
    NavigationBackHandler(
        state = navState,
        isBackEnabled = onBack != null,
        onBackCompleted = { onBack?.invoke() },
    )
}
