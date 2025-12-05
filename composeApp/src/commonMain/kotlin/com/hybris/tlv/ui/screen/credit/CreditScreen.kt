package com.hybris.tlv.ui.screen.credit

import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.component.container.Screen
import com.hybris.tlv.ui.theme.component.list.CreditList
import com.hybris.tlv.usecase.credit.model.Credit
import com.hybris.tlv.usecase.credit.model.CreditType
import com.hybris.tlv.usecase.translation.TranslationCache
import com.hybris.tlv.usecase.translation.model.Translation

@Composable
internal fun CreditScreen(store: Store<CreditState, Unit>) {
    val storeState by store.stateFlow.collectAsStateWithLifecycle()
    val creditsMap = storeState.credits.groupBy { it.type }
    val creators = creditsMap[CreditType.CREATOR].orEmpty()
    val sources = creditsMap[CreditType.SOURCE].orEmpty()
    val musics = creditsMap[CreditType.MUSIC].orEmpty()
    val supporters = creditsMap[CreditType.SUPPORTER].orEmpty()

    Screen(
        loading = storeState.loading,
        onBackClick = { store.back() },
        onHelpClick = { store.help() },
        onMusicClick = { store.toggleAudio() },
        onFeedbackClick = { store.feedback() },
    ) {
        CreditList(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 16.dp),
            creators = creators,
            sources = sources,
            musics = musics,
            supporters = supporters,
            id = { it.id },
            link = { it.link },
        )
    }
}

@Preview
@Composable
private fun CreditScreenLoadingPreview() = AppTheme {
    CreditScreen(
        store = Store(
            initialState = CreditState(
                loading = true,
                credits = emptyList()
            )
        )
    )
}

@Preview
@Composable
private fun CreditScreenPreview() = AppTheme {
    TranslationCache.set(
        translations = listOf(
            Translation(
                key = "credit_screen__creators",
                value = "Creators"
            ),
            Translation(
                key = "credit_screen__sources",
                value = "Sources"
            ),
            Translation(
                key = "credit_screen__music",
                value = "Music"
            ),
            Translation(
                key = "credit_screen__supporters",
                value = "Supporters"
            )
        )
    )
    CreditScreen(
        store = Store(
            initialState = CreditState(
                loading = false,
                credits = listOf(
                    Credit(
                        id = "engsoneca",
                        link = "https://ko-fi.com/engsoneca",
                        type = CreditType.CREATOR,
                    ),
                    Credit(
                        id = "NASA Exoplanet Archive DOIs 10.26133/NEA13 and 10.26133/NEA40",
                        link = "https://exoplanetarchive.ipac.caltech.edu/",
                        type = CreditType.SOURCE,
                    ),
                    Credit(
                        id = "OpenGameArt",
                        link = "https://opengameart.org/",
                        type = CreditType.MUSIC,
                    ),
                    Credit(
                        id = "You",
                        link = null,
                        type = CreditType.SUPPORTER,
                    ),
                    Credit(
                        id = "Yourself",
                        link = null,
                        type = CreditType.SUPPORTER,
                    ),
                    Credit(
                        id = "Irene",
                        link = null,
                        type = CreditType.SUPPORTER,
                    ),
                    Credit(
                        id = "Jim",
                        link = null,
                        type = CreditType.SUPPORTER,
                    ),
                )
            )
        )
    )
}
