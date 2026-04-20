package com.hybris.tlv.ui.screen.catastropheexplorer

import kotlinx.collections.immutable.persistentListOf
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hybris.tlv.domain.catastrophe.Catastrophe
import com.hybris.tlv.domain.translation.Translation
import com.hybris.tlv.ui.Preview
import com.hybris.tlv.ui.screen.Screen
import com.hybris.tlv.ui.screen.Store
import com.hybris.tlv.ui.theme.InjectTranslations
import com.hybris.tlv.ui.theme.component.list.CatastropheList
import com.hybris.tlv.ui.theme.getTranslation

@Composable
internal fun CatastropheExplorerScreen(store: Store<CatastropheExplorerState, Unit>) {
    val storeState by store.stateFlow.collectAsStateWithLifecycle()

    Screen(
        loading = storeState.loading,
        onHelpClick = null,
    ) {
        CatastropheList(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 16.dp),
            catastrophes = storeState.catastrophes,
            id = Catastrophe::id,
            name = { getTranslation(key = it.id) },
            description = { getTranslation(key = it.description) }
        )
    }
}

@Preview
@Composable
private fun CatastropheExplorerScreenLoadingPreview() = Preview {
    CatastropheExplorerScreen(
        store = Store(
            initialState = CatastropheExplorerState(
                loading = true,
                catastrophes = persistentListOf()
            )
        )
    )
}

@Preview
@Composable
private fun CatastropheExplorerScreenPreview() = Preview {
    InjectTranslations(
        translations = listOf(
            Translation(
                key = "catastrophe_screen__title",
                value = "Catastrophes"
            ),
        )
    )
    CatastropheExplorerScreen(
        store = Store(
            initialState = CatastropheExplorerState(
                loading = false,
                catastrophes = persistentListOf(
                    Catastrophe(
                        id = "Asteroid",
                        description = "Go Boom!",
                    ),
                    Catastrophe(
                        id = "Volcano",
                        description = "Go Bam!",
                    )
                )
            )
        )
    )
}
