package com.hybris.tlv.ui.screen.catastrophe

import kotlinx.coroutines.Job
import com.hybris.tlv.core.telemetry.Telemetry
import com.hybris.tlv.ui.navigation.Screen
import com.hybris.tlv.ui.screen.Store
import com.hybris.tlv.domain.usecase.catastrophe.CatastropheUseCases

internal class CatastropheStore(
    private val catastropheUseCases: CatastropheUseCases,
): Store<CatastropheState, CatastropheAction>(
    initialState = CatastropheState()
) {
    init {
        setup()
    }

    private fun setup(): Job = launch(id = "setup") {
        Telemetry.info(tag = TAG, message = "Setup")

        val selectedCatastrophe = catastropheUseCases.getRandomCatastrophe()
        if (selectedCatastrophe == null) {
            navigate(screen = Screen.Feedback(tag = TAG, message = "Invalid state: missing catastrophe on setup()"))
            return@launch
        }

        updateState {
            it.copy(
                loading = false,
                selectedCatastrophe = selectedCatastrophe,
            )
        }

        Telemetry.info(tag = TAG, message = "Setup complete")
    }

    override fun back(state: CatastropheState) {}

    override fun reducer(state: CatastropheState, action: CatastropheAction) {
        when (action) {
            CatastropheAction.Next -> navigate(screen = Screen.Game())
        }
    }

    companion object Companion {
        private const val TAG = "CatastropheStore"
    }
}
