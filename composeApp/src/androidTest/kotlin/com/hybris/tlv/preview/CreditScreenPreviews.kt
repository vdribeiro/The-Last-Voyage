package com.hybris.tlv.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.hybris.tlv.getStore
import com.hybris.tlv.ui.screen.credit.CreditScreen
import com.hybris.tlv.ui.screen.credit.CreditState
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.usecase.credit.model.Credit
import com.hybris.tlv.usecase.credit.model.CreditType

@Preview
@Composable
private fun CreditLoading() {
    AppTheme {
        CreditScreen(
            store = getStore(
                initialState = CreditState(
                    loading = true,
                    credits = emptyList()
                )
            )
        )
    }
}

@Preview
@Composable
private fun CreditList() {
    AppTheme {
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
}
