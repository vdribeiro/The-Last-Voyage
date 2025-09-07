package com.hybris.tlv.ui.screen.game.content

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.screen.game.GameAction
import com.hybris.tlv.ui.screen.game.GameState
import com.hybris.tlv.ui.screen.game.Tutorial
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.ui.theme.typography
import com.hybris.tlv.usecase.translation.getTranslation

@Composable
internal fun TutorialContent(store: Store<GameAction, GameState>) {
    val storeState by store.stateFlow.collectAsState()
    val title: String
    val description: String
    when (storeState.tutorial) {
        Tutorial.NO -> {
            title = remember { getTranslation(key = "tutorial_title") }
            description = remember { getTranslation(key = "tutorial_description") }
        }
        Tutorial.YES -> {
            title = remember { getTranslation(key = "tutorial_title") }
            description = remember { getTranslation(key = "tutorial_description") }
        }
        Tutorial.SHIP -> {
            title = remember { getTranslation(key = "tutorial_title") }
            description = remember { getTranslation(key = "tutorial_description") }
        }
        Tutorial.TRAVEL -> {
            title = remember { getTranslation(key = "tutorial_title") }
            description = remember { getTranslation(key = "tutorial_description") }
        }
        Tutorial.SYSTEM -> {
            title = remember { getTranslation(key = "tutorial_title") }
            description = remember { getTranslation(key = "tutorial_description") }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            style = typography.bodyLarge,
            text = title
        )
        Text(
            style = typography.bodyMedium,
            text = description
        )
    }
}
