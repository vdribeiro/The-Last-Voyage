package com.hybris.tlv.ui.navigation

import kotlinx.serialization.Serializable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.toRoute
import com.hybris.tlv.telemetry.Telemetry
import com.hybris.tlv.usecase.ship.model.Ship

@Serializable
internal sealed interface Screen {
    @Serializable
    data object Splash: Screen
    @Serializable
    data object Cheats: Screen
    @Serializable
    data object MainMenu: Screen
    @Serializable
    data object Help: Screen
    @Serializable
    data class Feedback(val tag: String? = null, val message: String? = null): Screen
    @Serializable
    data object NewGame: Screen
    @Serializable
    data class Tutorial(val newGame: Boolean = false): Screen
    @Serializable
    data class Game(val ship: Ship? = null): Screen
    @Serializable
    data class Event(val ship: Ship? = null): Screen
    @Serializable
    data object GameOver: Screen
    @Serializable
    data object StellarExplorer: Screen
    @Serializable
    data object Score: Screen
    @Serializable
    data object Achievement: Screen
    @Serializable
    data object Credit: Screen
}

internal fun NavBackStackEntry.toScreen(): Screen? = runCatching {
    when {
        destination.hasRoute<Screen.Splash>() -> toRoute<Screen.Splash>()
        destination.hasRoute<Screen.Cheats>() -> toRoute<Screen.Cheats>()
        destination.hasRoute<Screen.MainMenu>() -> toRoute<Screen.MainMenu>()
        destination.hasRoute<Screen.Help>() -> toRoute<Screen.Help>()
        destination.hasRoute<Screen.Feedback>() -> toRoute<Screen.Feedback>()
        destination.hasRoute<Screen.NewGame>() -> toRoute<Screen.NewGame>()
        destination.hasRoute<Screen.Tutorial>() -> toRoute<Screen.Tutorial>()
        destination.hasRoute<Screen.Game>() -> toRoute<Screen.Game>()
        destination.hasRoute<Screen.Event>() -> toRoute<Screen.Event>()
        destination.hasRoute<Screen.GameOver>() -> toRoute<Screen.GameOver>()
        destination.hasRoute<Screen.StellarExplorer>() -> toRoute<Screen.StellarExplorer>()
        destination.hasRoute<Screen.Score>() -> toRoute<Screen.Score>()
        destination.hasRoute<Screen.Achievement>() -> toRoute<Screen.Achievement>()
        destination.hasRoute<Screen.Credit>() -> toRoute<Screen.Credit>()
        else -> null
    }
}.onFailure { Telemetry.error(tag = TAG, message = "Unable to get screen", throwable = it) }.getOrNull()

private const val TAG = "Screen"
