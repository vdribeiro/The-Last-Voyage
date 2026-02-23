package com.hybris.tlv.ui.screen.credit

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
    val creditsMap = storeState.credits.groupBy { it.type }
    val creators = creditsMap[CreditType.CREATOR].orEmpty()
    val sources = creditsMap[CreditType.SOURCE].orEmpty()
    val musics = creditsMap[CreditType.MUSIC].orEmpty()
    val supporters = creditsMap[CreditType.SUPPORTER].orEmpty()

    Screen(
        store = store,
        loading = storeState.loading,
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
private fun CreditScreenLoadingPreview() = Preview {
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
