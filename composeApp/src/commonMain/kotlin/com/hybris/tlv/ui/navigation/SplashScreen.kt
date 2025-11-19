package com.hybris.tlv.ui.navigation

import kotlinx.serialization.Serializable
import androidx.navigation.NavGraphBuilder
import com.hybris.tlv.config.ConfigManager
import com.hybris.tlv.ui.screen.splash.SplashAction
import com.hybris.tlv.ui.screen.splash.SplashScreen
import com.hybris.tlv.ui.screen.splash.SplashState
import com.hybris.tlv.ui.screen.splash.SplashStore
import com.hybris.tlv.usecase.UseCases

internal fun NavGraphBuilder.splashScreen(
    config: ConfigManager,
    useCases: UseCases
) {
    graph<SplashScreen, SplashState, SplashAction>(
        store = {
            SplashStore(
                config = config,
                archiveUseCases = useCases.archive,
                translateUseCases = useCases.translation,
                learningUseCases = useCases.learning,
                catastropheUseCases = useCases.catastrophe,
                shipUseCases = useCases.ship,
                spaceUseCases = useCases.space,
                eventUseCases = useCases.event,
                achievementUseCases = useCases.achievement,
                creditUseCases = useCases.credit
            )
        },
        screen = { SplashScreen(store = it) }
    )
}

@Serializable
internal data object SplashScreen: Screen
