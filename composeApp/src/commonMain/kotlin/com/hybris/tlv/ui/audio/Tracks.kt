package com.hybris.tlv.ui.audio

import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import com.hybris.tlv.core.telemetry.Telemetry
import com.hybris.tlv.data.resource.AudioResource
import com.hybris.tlv.ui.navigation.Screen

/**
 * Determines the appropriate playlist based on the current navigation destination.
 * Null means that the playback should remain unchanged.
 */
internal fun getTracks(destination: NavDestination?): List<AudioResource>? = runCatching {
    when {
        destination == null ||
                destination.hasRoute<Screen.Cheat>() ||
                destination.hasRoute<Screen.Help>() ||
                destination.hasRoute<Screen.Feedback>() ||
                destination.hasRoute<Screen.Tutorial>() -> null

        destination.hasRoute<Screen.Splash>() ||
                destination.hasRoute<Screen.MainMenu>() ||
                destination.hasRoute<Screen.NewGame>() ||
                destination.hasRoute<Screen.StellarExplorer>() ||
                destination.hasRoute<Screen.Score>() ||
                destination.hasRoute<Screen.Achievement>() ||
                destination.hasRoute<Screen.Credit>() -> listOf(
            AudioResource.VilleSeppanen
        )

        destination.hasRoute<Screen.Catastrophe>() ||
                destination.hasRoute<Screen.Game>() ||
                destination.hasRoute<Screen.Event>() -> listOf(
            AudioResource.BlindShift,
            AudioResource.Graduality,
            AudioResource.LedTwilight,
            AudioResource.NeonSky,
            AudioResource.RainInSpace,
            AudioResource.SpaceGras
        )

        destination.hasRoute<Screen.GameOver>() -> listOf(
            AudioResource.Space
        )

        else -> null
    }?.shuffled()
}.onFailure {
    Telemetry.error(tag = TAG, message = "Unable to get tracks", throwable = it)
}.getOrNull()

private const val TAG = "Tracks"
