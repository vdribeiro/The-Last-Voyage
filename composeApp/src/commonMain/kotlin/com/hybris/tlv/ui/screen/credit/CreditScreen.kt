package com.hybris.tlv.ui.screen.credit

import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.ui.store.getStore
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.component.container.Screen
import com.hybris.tlv.ui.theme.component.list.CreditList
import com.hybris.tlv.usecase.credit.model.Credit
import com.hybris.tlv.usecase.credit.model.CreditType

@Composable
internal fun CreditScreen(store: Store<CreditState, Unit>) {
    val storeState by store.stateFlow.collectAsState()

    Screen(
        loading = storeState.loading,
        onBackClick = { store.back() },
        onHelpClick = { store.help() },
        onMusicClick = { store.toggleAudio() },
        onFeedbackClick = { store.feedback() },
    ) {
        CreditList(
            credits = storeState.credits,
            id = { it.id },
            link = { it.link },
            type = { it.type }
        )
    }
}

@Preview
@Composable
private fun CreditLoadingPreview() = AppTheme {
    CreditScreen(
        store = getStore(
            initialState = CreditState(
                loading = true,
                credits = emptyList()
            )
        )
    )
}

@Preview
@Composable
private fun CreditListPreview() = AppTheme {
    CreditScreen(
        store = getStore(
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
