package com.hybris.tlv.ui.screen.catastrophe

import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hybris.tlv.usecase.catastrophe.model.Catastrophe
import com.hybris.tlv.usecase.translation.TranslationCache
import com.hybris.tlv.usecase.translation.model.Translation

@Composable
internal fun CatastropheScreen(store: com.hybris.tlv.ui.screen.Store<CatastropheState, CatastropheAction>) {
    val storeState by store.stateFlow.collectAsStateWithLifecycle()
    val selectedCatastrophe = storeState.selectedCatastrophe
    val continueTranslation = _root_ide_package_.com.hybris.tlv.ui.theme.getTranslation(key = "new_game_screen__continue")

    _root_ide_package_.com.hybris.tlv.ui.screen.Screen(
        store = store,
        loading = storeState.loading,
        back = false,
        bottomBar = {
            if (storeState.loading) return@Screen
            _root_ide_package_.com.hybris.tlv.ui.theme.component.bottombar.ButtonsBar(
                buttons = listOf(
                    _root_ide_package_.com.hybris.tlv.ui.theme.component.bottombar.BottomButton(
                        id = continueTranslation,
                        text = continueTranslation,
                        onClick = { store.send(action = CatastropheAction.Next) }
                    )
                )
            )
        },
    ) {
        _root_ide_package_.com.hybris.tlv.ui.theme.component.container.TypewriterContent(
            modifier = Modifier
                .testTag(tag = "new_game_content")
                .fillMaxSize()
                .padding(all = 16.dp),
            title = selectedCatastrophe?.let { _root_ide_package_.com.hybris.tlv.ui.theme.getTranslation(key = it.id) },
            text = selectedCatastrophe?.let { _root_ide_package_.com.hybris.tlv.ui.theme.getTranslation(key = it.description) }
        )
    }
}

@Preview
@Composable
private fun CatastropheScreenLoadingPreview() = _root_ide_package_.com.hybris.tlv.ui.theme.AppTheme {
    TranslationCache.set(
        translations = listOf(
            Translation(
                key = "new_game_screen__continue",
                value = "Continue"
            ),
        )
    )
    CatastropheScreen(
        store = _root_ide_package_.com.hybris.tlv.ui.screen.Store(
            initialState = CatastropheState(
                loading = true,
                selectedCatastrophe = null,
            )
        )
    )
}

@Preview
@Composable
private fun CatastropheScreenStartPreview() = _root_ide_package_.com.hybris.tlv.ui.theme.AppTheme {
    TranslationCache.set(
        translations = listOf(
            Translation(
                key = "new_game_screen__continue",
                value = "Continue"
            ),
        )
    )
    CatastropheScreen(
        store = _root_ide_package_.com.hybris.tlv.ui.screen.Store(
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
