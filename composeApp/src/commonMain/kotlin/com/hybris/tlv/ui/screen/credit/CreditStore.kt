package com.hybris.tlv.ui.screen.credit

import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Job
import com.hybris.tlv.core.telemetry.Telemetry
import com.hybris.tlv.domain.usecase.credit.CreditUseCases
import com.hybris.tlv.domain.usecase.credit.model.CreditType
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

        val credits = creditUseCases.getCredits().groupBy { it.type }
        val creators = credits[CreditType.CREATOR].orEmpty().toPersistentList()
        val sources = credits[CreditType.SOURCE].orEmpty().toPersistentList()
        val musics = credits[CreditType.MUSIC].orEmpty().toPersistentList()
        val supporters = credits[CreditType.SUPPORTER].orEmpty().toPersistentList()

        updateState {
            it.copy(
                loading = false,
                creators = creators,
                sources = sources,
                musics = musics,
                supporters = supporters
            )
        }

        Telemetry.info(tag = TAG, message = "Setup complete")
    }

    companion object {
        private const val TAG = "CreditStore"
    }
}
