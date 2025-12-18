package com.hybris.tlv.navigation.graph

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.hybris.tlv.config.ConfigManager
import com.hybris.tlv.navigation.Screen
import com.hybris.tlv.screen.splash.SplashScreen
import com.hybris.tlv.screen.splash.SplashStore
import com.hybris.tlv.usecase.UseCases

internal fun NavGraphBuilder.splashScreen(
    config: ConfigManager,
    useCases: UseCases
) = composable<Screen.Splash> {
    SplashScreen(store = viewModel {
        SplashStore(
            config = config,
            syncUseCases = useCases.sync
        )
    })
}
