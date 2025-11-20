package com.hybris.tlv.ui.screen.credit

import kotlinx.coroutines.Job
import com.hybris.tlv.telemetry.Telemetry
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.credit.CreditUseCases

internal class CreditStore(
    private val creditUseCases: CreditUseCases
): Store<CreditState, Unit>(
    initialState = CreditState()
) {
    init {
        setup()
    }

    private fun setup(): Job = launch {
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
