package com.hybris.tlv.ui.navigation.graph

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.hybris.tlv.ui.navigation.Screen
import com.hybris.tlv.ui.screen.StoreFactory
import com.hybris.tlv.ui.screen.score.ScoreScreen

internal fun NavGraphBuilder.scoreScreen(storeFactory: StoreFactory) =
    composable<Screen.Score> {
        ScoreScreen(store = viewModel { storeFactory.getScoreStore() })
    }


