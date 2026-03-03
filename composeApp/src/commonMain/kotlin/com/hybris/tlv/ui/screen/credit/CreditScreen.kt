package com.hybris.tlv.ui.screen.credit

import kotlinx.collections.immutable.persistentListOf
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hybris.tlv.domain.usecase.credit.model.Credit
import com.hybris.tlv.domain.usecase.credit.model.CreditType
import com.hybris.tlv.domain.usecase.translation.model.Translation
import com.hybris.tlv.ui.Preview
import com.hybris.tlv.ui.screen.Screen
import com.hybris.tlv.ui.screen.Store
import com.hybris.tlv.ui.theme.InjectTranslations
import com.hybris.tlv.ui.theme.component.list.CreditList

@Composable
internal fun CreditScreen(store: Store<CreditState, Unit>) {
    val storeState by store.stateFlow.collectAsStateWithLifecycle()

    Screen(
        loading = storeState.loading,
    ) {
        CreditList(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 16.dp),
            creators = storeState.creators,
            sources = storeState.sources,
            musics = storeState.musics,
            supporters = storeState.supporters,
            id = Credit::id,
            link = Credit::link,
        )
    }
}

@Preview
@Composable
private fun CreditScreenLoadingPreview() = Preview {
    CreditScreen(
        store = Store(
            initialState = CreditState(
                loading = true,
                creators = persistentListOf(),
                sources = persistentListOf(),
                musics = persistentListOf(),
                supporters = persistentListOf(),
            )
        )
    )
}

@Preview
@Composable
private fun CreditScreenPreview() = Preview {
    InjectTranslations(
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
                creators = persistentListOf(
                    Credit(
                        id = "engsoneca",
                        link = "https://ko-fi.com/engsoneca",
                        type = CreditType.CREATOR,
                    )
                ),
                sources = persistentListOf(
                    Credit(
                        id = "NASA Exoplanet Archive DOIs 10.26133/NEA13 and 10.26133/NEA40",
                        link = "https://exoplanetarchive.ipac.caltech.edu/",
                        type = CreditType.SOURCE,
                    )
                ),
                musics = persistentListOf(
                    Credit(
                        id = "OpenGameArt",
                        link = "https://opengameart.org/",
                        type = CreditType.MUSIC,
                    )
                ),
                supporters = persistentListOf(
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
                    )
                )
            )
        )
    )
}
