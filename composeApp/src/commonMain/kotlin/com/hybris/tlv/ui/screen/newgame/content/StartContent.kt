package com.hybris.tlv.ui.screen.newgame.content

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.component.TypewriterText
import com.hybris.tlv.ui.screen.newgame.NewGameAction
import com.hybris.tlv.ui.screen.newgame.NewGameState
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.translation.getTranslation

@Composable
internal fun StartContent(store: Store<NewGameAction, NewGameState>) {
    val storeState by store.stateFlow.collectAsState()
    val catastrophe = storeState.selectedCatastrophe ?: return

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = getTranslation(key = catastrophe.name),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(height = 16.dp))

        TypewriterText(
            modifier = Modifier
                .weight(weight = 1f)
                .fillMaxWidth(),
            text = getTranslation(key = catastrophe.description)
        )

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            colors = ButtonDefaults.buttonColors(contentColor = Color.White),
            onClick = { store.send(action = NewGameAction.StartGame) }
        ) {
            Text(text = getTranslation(key = "new_game_screen__start"))
        }
    }
}
