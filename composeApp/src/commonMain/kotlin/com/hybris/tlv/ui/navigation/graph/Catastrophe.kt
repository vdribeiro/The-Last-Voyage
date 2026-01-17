package com.hybris.tlv.ui.navigation.graph

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.hybris.tlv.ui.navigation.Screen
import com.hybris.tlv.ui.screen.catastrophe.CatastropheScreen
import com.hybris.tlv.ui.screen.catastrophe.CatastropheStore
import com.hybris.tlv.domain.usecase.UseCases

internal fun NavGraphBuilder.catastropheScreen(
    useCases: UseCases
) = composable<Screen.Catastrophe> {
    CatastropheScreen(store = viewModel {
        CatastropheStore(
            catastropheUseCases = useCases.catastrophe
        )
    })
}
