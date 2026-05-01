package com.hybris.tlv.ui.screen.splash

import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import com.hybris.tlv.core.platform.Platform
import com.hybris.tlv.core.platform.platform
import com.hybris.tlv.core.telemetry.Telemetry
import com.hybris.tlv.data.config.ConfigManager
import com.hybris.tlv.domain.usecase.sync.SyncUseCases
import com.hybris.tlv.test.VisibleForTesting
import com.hybris.tlv.ui.navigation.Screen
import com.hybris.tlv.ui.screen.Store

internal class SplashStore(
    private val reset: Boolean,
    private val config: ConfigManager,
    private val syncUseCases: SyncUseCases
): Store<SplashState, SplashAction>(
    initialState = SplashState()
) {
    @VisibleForTesting
    internal var setupJob: Job? = null
    @VisibleForTesting
    internal var watchDogJob: Job? = null

    init {
        watchDogJob = launch(id = "watchDog") {
            delay(timeMillis = 5000)
            updateState { it.copy(showFeedback = true) }
        }
        setupJob = setup()
    }

    private fun setup(): Job = launch(id = "setup") {
        Telemetry.info(tag = TAG, message = "Setup")

        val result = syncUseCases.sync(reset = reset) { progress ->
            watchDogJob?.cancel()
            updateState {
                it.copy(
                    progress = progress,
                    showFeedback = false
                )
            }
        }
        Telemetry.info(tag = TAG, message = "Sync result: $result")
        delay(timeMillis = 1000L) // prevent UI flickering for fast syncs and also allow user to see the sweet 100%

        Telemetry.info(tag = TAG, message = "Setup complete")

        when {
            config.preferences.showIntro -> {
                config.setPreferences { it.copy(showIntro = false) }.savePreferences()
                updateState { it.copy(loading = false, currentContent = Content.INTRO) }
            }

            platform == Platform.Web -> updateState { it.copy(loading = false) }
            else -> navigate(screen = Screen.MainMenu)
        }
    }

    override fun reducer(state: SplashState, action: SplashAction) {
        when (action) {
            SplashAction.Feedback -> navigate(screen = Screen.Feedback(tag = null, message = null))
            SplashAction.Next -> if (!state.loading) navigate(screen = Screen.MainMenu)
        }
    }

    companion object {
        private const val TAG = "SplashStore"
    }
}
