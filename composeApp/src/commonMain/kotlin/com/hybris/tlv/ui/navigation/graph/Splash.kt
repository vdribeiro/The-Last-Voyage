package com.hybris.tlv.ui.navigation.graph

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.hybris.tlv.data.config.ConfigManager
import com.hybris.tlv.ui.navigation.Screen
import com.hybris.tlv.usecase.UseCases

internal fun NavGraphBuilder.splashScreen(
    config: ConfigManager,
    useCases: UseCases
) = composable<Screen.Splash> {
    val screen = it.toRoute<Screen.Splash>()
    _root_ide_package_.com.hybris.tlv.ui.screen.splash.SplashScreen(store = viewModel {
        _root_ide_package_.com.hybris.tlv.ui.screen.splash.SplashStore(
            reset = screen.reset,
            config = config,
            syncUseCases = useCases.sync
        )
    })
}
