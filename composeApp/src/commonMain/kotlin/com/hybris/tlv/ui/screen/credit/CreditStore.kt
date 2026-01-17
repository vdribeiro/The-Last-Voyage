package com.hybris.tlv.ui.screen.credit

import kotlinx.coroutines.Job
import com.hybris.tlv.core.telemetry.Telemetry
import com.hybris.tlv.domain.usecase.credit.CreditUseCases
import com.hybris.tlv.ui.screen.Store

internal class CreditStore(
    private val creditUseCases: CreditUseCases
): Store<CreditState, Unit>(
    initialState = CreditState()
) {
    init {
        setup()
    }

    private fun setup(): Job = launch(id = "setup") {
        Telemetry.info(tag = TAG, message = "Setup")

        val credits = creditUseCases.getCredits()
        updateState {
            it.copy(
                loading = false,
                credits = credits
            )
        }

        Telemetry.info(tag = TAG, message = "Setup complete")
    }

    companion object {
        private const val TAG = "CreditStore"
    }
}
