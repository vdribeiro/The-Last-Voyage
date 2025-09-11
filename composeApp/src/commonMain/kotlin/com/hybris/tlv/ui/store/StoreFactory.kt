package com.hybris.tlv.ui.store

import com.hybris.tlv.config.ConfigManager
import com.hybris.tlv.flow.Dispatcher
import com.hybris.tlv.ui.navigation.NavigationManager
import com.hybris.tlv.ui.screen.feedback.FeedbackAction
import com.hybris.tlv.ui.screen.feedback.FeedbackState
import com.hybris.tlv.ui.screen.feedback.createFeedbackStore
import com.hybris.tlv.ui.screen.mainmenu.MainMenuAction
import com.hybris.tlv.ui.screen.mainmenu.MainMenuState
import com.hybris.tlv.ui.screen.mainmenu.createMainMenuStore
import com.hybris.tlv.ui.screen.splash.SplashAction
import com.hybris.tlv.ui.screen.splash.SplashState
import com.hybris.tlv.ui.screen.splash.createSplashStore
import com.hybris.tlv.usecase.UseCases

internal class StoreFactory(
    private val dispatcher: Dispatcher,
    private val navigation: NavigationManager,
    private val config: ConfigManager,
    private val useCases: UseCases
) {
    fun createSplashStore(state: Any? = null): Store<SplashAction, SplashState> {
        val state = state as? SplashState ?: SplashState()
        return createSplashStore(
            dispatcher = dispatcher,
            navigation = navigation,
            initialState = state,
            syncUseCases = useCases.sync
        ).apply { setup(state = state) }
    }

    fun createMainMenuStore(state: Any? = null): Store<MainMenuAction, MainMenuState> {
        val state = state as? MainMenuState ?: MainMenuState()
        return createMainMenuStore(
            dispatcher = dispatcher,
            navigation = navigation,
            initialState = state,
            config = config,
            gameSessionUseCases = useCases.gameSession,
            learningUseCases = useCases.learning
        ).apply { setup(state = state) }
    }

    fun createFeedbackStore(state: Any? = null): Store<FeedbackAction, FeedbackState> {
        val state = state as? FeedbackState ?: FeedbackState()
        return createFeedbackStore(
            dispatcher = dispatcher,
            navigation = navigation,
            initialState = state
        ).apply { setup(state = state) }
    }
}