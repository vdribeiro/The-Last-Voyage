package com.hybris.tlv.ui.navigation.graph

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.hybris.tlv.data.config.ConfigManager
import com.hybris.tlv.domain.usecase.UseCases
import com.hybris.tlv.ui.navigation.Screen
import com.hybris.tlv.ui.screen.splash.SplashScreen
import com.hybris.tlv.ui.screen.splash.SplashStore

internal fun NavGraphBuilder.splashScreen(
    config: ConfigManager,
    useCases: UseCases
) = composable<Screen.Splash> {
    val screen = it.toRoute<Screen.Splash>()
    SplashScreen(store = viewModel {
        SplashStore(
            reset = screen.reset,
            config = config,
            syncUseCases = useCases.sync
        )
    })
}
