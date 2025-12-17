package com.hybris.tlv.ui.navigation.graph

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.hybris.tlv.config.ConfigManager
import com.hybris.tlv.ui.navigation.Screen
import com.hybris.tlv.ui.screen.splash.SplashScreen
import com.hybris.tlv.ui.screen.splash.SplashStore
import com.hybris.tlv.usecase.UseCases

internal fun NavGraphBuilder.splashScreen(
    config: ConfigManager,
    useCases: UseCases
) = composable<Screen.Splash> {
    SplashScreen(store = viewModel {
        SplashStore(
            config = config,
            archiveUseCases = useCases.archive,
            translationUseCases = useCases.translation,
            catastropheUseCases = useCases.catastrophe,
            shipUseCases = useCases.ship,
            spaceUseCases = useCases.space,
            eventUseCases = useCases.event,
            achievementUseCases = useCases.achievement,
            creditUseCases = useCases.credit
        )
    })
}
