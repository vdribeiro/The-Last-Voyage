package com.hybris.tlv.ui.screen.catastrophe

import kotlinx.collections.immutable.persistentListOf
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hybris.tlv.domain.usecase.catastrophe.model.Catastrophe
import com.hybris.tlv.domain.usecase.translation.model.Translation
import com.hybris.tlv.ui.Preview
import com.hybris.tlv.ui.screen.Screen
import com.hybris.tlv.ui.screen.Store
import com.hybris.tlv.ui.theme.InjectTranslations
import com.hybris.tlv.ui.theme.component.bottombar.ButtonsBar
import com.hybris.tlv.ui.theme.component.container.TypewriterContent
import com.hybris.tlv.ui.theme.getTranslation

@Composable
internal fun CatastropheScreen(store: Store<CatastropheState, CatastropheAction>) {
    val storeState by store.stateFlow.collectAsStateWithLifecycle()
    val selectedCatastrophe = storeState.selectedCatastrophe
    val continueTranslation = getTranslation(key = "new_game_screen__continue")

    Screen(
        loading = storeState.loading,
        onBackClick = null,
        bottomBar = {
            if (storeState.loading) return@Screen
            ButtonsBar(
                buttons = persistentListOf(continueTranslation),
                id = { it },
                text = { it },
                onClick = { store.send(action = CatastropheAction.Next) }
            )
        },
    ) {
        TypewriterContent(
            modifier = Modifier
                .testTag(tag = "new_game_content")
                .fillMaxSize()
                .padding(all = 16.dp),
            title = selectedCatastrophe?.let { getTranslation(key = it.id) },
            text = selectedCatastrophe?.let {
                "${getTranslation(key = it.description)}\n\n${getTranslation(key = "catastrophe__escape")}"
            }
        )
    }
}

@Preview
@Composable
private fun CatastropheScreenLoadingPreview() = Preview {
    InjectTranslations(
        translations = listOf(
            Translation(
                key = "new_game_screen__continue",
                value = "Continue"
            ),
        )
    )
    CatastropheScreen(
        store = Store(
            initialState = CatastropheState(
                loading = true,
                selectedCatastrophe = null,
            )
        )
    )
}

@Preview
@Composable
private fun CatastropheScreenStartPreview() = Preview {
    InjectTranslations(
        translations = listOf(
            Translation(
                key = "new_game_screen__continue",
                value = "Continue"
            ),
        )
    )
    CatastropheScreen(
        store = Store(
            initialState = CatastropheState(
                loading = false,
                selectedCatastrophe = Catastrophe(
                    id = "Asteroid Impact",
                    description = "A massive asteroid collides with Earth. The impact wipes out most life on the planet.",
                ),
            )
        )
    )
}
