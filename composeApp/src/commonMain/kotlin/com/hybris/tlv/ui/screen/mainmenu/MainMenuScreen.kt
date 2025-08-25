package com.hybris.tlv.ui.screen.mainmenu

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.translation.getTranslation
import org.jetbrains.compose.resources.painterResource
import thelastvoyage.composeapp.generated.resources.Res
import thelastvoyage.composeapp.generated.resources.ic_launcher_foreground
import thelastvoyage.composeapp.generated.resources.support_me_on_kofi_badge_beige

@Composable
internal fun MainMenuScreen(store: Store<MainMenuAction, MainMenuState>) {
    val storeState by store.stateFlow.collectAsState()
    val uriHandler = LocalUriHandler.current

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                storeState.developerCorner?.let {
                    Text(
                        modifier = Modifier
                            .size(size = 100.dp)
                            .wrapContentHeight(align = Alignment.CenterVertically)
                            .clickable { uriHandler.openUri(uri = it) },
                        text = "Developer's Corner",
                        style = MaterialTheme.typography.titleSmall,
                    )
                }
                storeState.tip?.let {
                    Image(
                        modifier = Modifier
                            .size(size = 100.dp)
                            .clickable { uriHandler.openUri(uri = it) },
                        painter = painterResource(resource = Res.drawable.support_me_on_kofi_badge_beige),
                        contentDescription = "Tip",
                        contentScale = ContentScale.Fit,
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(paddingValues = innerPadding)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(state = rememberScrollState())
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
                    modifier = Modifier.clickable { store.send(action = MainMenuAction.NewGame) },
                    text = getTranslation(key = "main_menu_screen__new_game"),
                    style = MaterialTheme.typography.headlineMedium,
                )
                if (storeState.ongoingGameSession) {
                    Spacer(modifier = Modifier.height(height = 16.dp))
                    Text(
                        modifier = Modifier.clickable { store.send(action = MainMenuAction.Continue) },
                        text = getTranslation(key = "main_menu_screen__continue"),
                        style = MaterialTheme.typography.headlineMedium,
                    )
                }
                Spacer(modifier = Modifier.height(height = 16.dp))
                Text(
                    modifier = Modifier.clickable { store.send(action = MainMenuAction.Learn) },
                    text = getTranslation(key = "main_menu_screen__learn"),
                    style = MaterialTheme.typography.headlineMedium,
                )
                Spacer(modifier = Modifier.height(height = 16.dp))
                Text(
                    modifier = Modifier.clickable { store.send(action = MainMenuAction.Scores) },
                    text = getTranslation(key = "main_menu_screen__scores"),
                    style = MaterialTheme.typography.headlineMedium,
                )
                //Spacer(modifier = Modifier.height(height = 16.dp))
                //Text(
                //    modifier = Modifier.clickable { store.send(action = MainMenuAction.Achievements) },
                //    text = getTranslation(key = "main_menu_screen__achievements"),
                //    style = MaterialTheme.typography.headlineMedium,
                //)
                Spacer(modifier = Modifier.height(height = 16.dp))
                Text(
                    modifier = Modifier.clickable { store.send(action = MainMenuAction.Credits) },
                    text = getTranslation(key = "main_menu_screen__credits"),
                    style = MaterialTheme.typography.headlineMedium,
                )
            }
        }
    }
}
