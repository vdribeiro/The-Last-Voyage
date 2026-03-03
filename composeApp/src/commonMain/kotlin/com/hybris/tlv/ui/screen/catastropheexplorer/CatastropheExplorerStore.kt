package com.hybris.tlv.ui.screen.catastropheexplorer

import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Job
import com.hybris.tlv.core.telemetry.Telemetry
import com.hybris.tlv.domain.usecase.catastrophe.CatastropheUseCases
import com.hybris.tlv.ui.screen.Store

internal class CatastropheExplorerStore(
    private val catastropheUseCases: CatastropheUseCases
): Store<CatastropheExplorerState, Unit>(
    initialState = CatastropheExplorerState()
) {
    init {
        setup()
    }

    private fun setup(): Job = launch(id = "setup") {
        Telemetry.info(tag = TAG, message = "Setup")

        val catastrophes = catastropheUseCases.getCatastrophes().toPersistentList()
        updateState {
            it.copy(
                loading = false,
                catastrophes = catastrophes
            )
        }

        Telemetry.info(tag = TAG, message = "Setup complete")
    }

    companion object {
        private const val TAG = "CatastropheExplorerStore"
    }
}
