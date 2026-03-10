package com.hybris.tlv.ui.navigation.graph

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.hybris.tlv.ui.navigation.Screen
import com.hybris.tlv.ui.screen.StoreFactory
import com.hybris.tlv.ui.screen.splash.SplashScreen

internal fun NavGraphBuilder.splashScreen(storeFactory: StoreFactory) =
    composable<Screen.Splash> {
        val screen = it.toRoute<Screen.Splash>()
        SplashScreen(store = viewModel { storeFactory.getSplashStore(reset = screen.reset) })
    }
