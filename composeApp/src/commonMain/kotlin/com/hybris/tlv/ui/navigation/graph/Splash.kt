package com.hybris.tlv.ui.navigation.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.hybris.tlv.config.ConfigManager
import com.hybris.tlv.ui.navigation.Screen
import com.hybris.tlv.ui.navigation.graph
import com.hybris.tlv.ui.screen.splash.SplashScreen
import com.hybris.tlv.ui.screen.splash.SplashStore
import com.hybris.tlv.usecase.UseCases

internal fun NavGraphBuilder.splashScreen(
    navController: NavHostController,
    config: ConfigManager,
    useCases: UseCases
) = graph<Screen.Splash, SplashStore>(
    navController = navController,
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
