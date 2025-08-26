package com.hybris.tlv.ui.screen.learn.content

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.screen.learn.LearnAction
import com.hybris.tlv.ui.screen.learn.LearnState
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.translation.getTranslation
import org.jetbrains.compose.resources.painterResource
import thelastvoyage.composeapp.generated.resources.Res
import thelastvoyage.composeapp.generated.resources.ic_launcher_foreground

@Composable
internal fun MenuContent(store: Store<LearnAction, LearnState>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .size(size = 160.dp)
                .clip(shape = CircleShape),
            painter = painterResource(resource = Res.drawable.ic_launcher_foreground),
            contentDescription = "Logo",
            contentScale = ContentScale.Crop,
        )
        Text(
            text = getTranslation(key = "app_name"),
            style = MaterialTheme.typography.headlineLarge,
        )
        Spacer(modifier = Modifier.height(height = 64.dp))
        Text(
            modifier = Modifier.clickable { store.send(action = LearnAction.StellarExplorer) },
            text = getTranslation(key = "learn_screen__stellar_explorer"),
            style = MaterialTheme.typography.headlineMedium,
        )
        Spacer(modifier = Modifier.height(height = 16.dp))
        Text(
            modifier = Modifier.clickable { store.send(action = LearnAction.HostTypes) },
            text = getTranslation(key = "learn_screen__star_types"),
            style = MaterialTheme.typography.headlineMedium,
        )
        Spacer(modifier = Modifier.height(height = 16.dp))
        Text(
            modifier = Modifier.clickable { store.send(action = LearnAction.PlanetTypes) },
            text = getTranslation(key = "learn_screen__planet_types"),
            style = MaterialTheme.typography.headlineMedium,
        )
        Spacer(modifier = Modifier.height(height = 16.dp))
        Text(
            modifier = Modifier.clickable { store.send(action = LearnAction.Properties) },
            text = getTranslation(key = "learn_screen__properties"),
            style = MaterialTheme.typography.headlineMedium,
        )
        Spacer(modifier = Modifier.height(height = 16.dp))
        Text(
            modifier = Modifier.clickable { store.send(action = LearnAction.Mechanics) },
            text = getTranslation(key = "learn_screen__mechanics"),
            style = MaterialTheme.typography.headlineMedium,
        )
        Spacer(modifier = Modifier.height(height = 16.dp))
        Text(
            modifier = Modifier.clickable { store.send(action = LearnAction.Habitability) },
            text = getTranslation(key = "learn_screen__habitability"),
            style = MaterialTheme.typography.headlineMedium,
        )
    }
}
