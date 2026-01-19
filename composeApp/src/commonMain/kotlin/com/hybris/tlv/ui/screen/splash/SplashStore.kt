package com.hybris.tlv.ui.screen.splash

import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
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

    init {
        setupJob = setup()
    }

    private fun setup(): Job = launch(id = "setup") {
        Telemetry.info(tag = TAG, message = "Setup")

        val result = syncUseCases.sync(reset = reset) { progress -> updateState { it.copy(progress = progress) } }
        Telemetry.info(tag = TAG, message = "Sync result: $result")
        delay(timeMillis = 1000L) // prevent UI flickering for fast syncs and also allow user to see the sweet 100% for a short time

        Telemetry.info(tag = TAG, message = "Setup complete")

        if (!config.preferences.value.showIntro) navigate(screen = Screen.MainMenu) else showIntro()
    }

    private suspend fun showIntro() {
        config.setPreferences { it.copy(showIntro = false) }
        updateState { it.copy(loading = false, currentContent = Content.INTRO) }
    }

    override fun back(state: SplashState) {}

    override fun reducer(state: SplashState, action: SplashAction) {
        when (action) {
            SplashAction.Next -> navigate(screen = Screen.MainMenu)
        }
    }

    companion object {
        private const val TAG = "SplashStore"
    }
}
