package com.hybris.tlv.ui.screen.mainmenu

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.screen.mainmenu.content.HabitabilityContent
import com.hybris.tlv.ui.screen.mainmenu.content.HostsContent
import com.hybris.tlv.ui.screen.mainmenu.content.LearnContent
import com.hybris.tlv.ui.screen.mainmenu.content.MainMenuContent
import com.hybris.tlv.ui.screen.mainmenu.content.MechanicsContent
import com.hybris.tlv.ui.screen.mainmenu.content.PlanetsContent
import com.hybris.tlv.ui.screen.mainmenu.content.PropertiesContent
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.translation.getTranslation
import org.jetbrains.compose.resources.painterResource
import thelastvoyage.composeapp.generated.resources.Res
import thelastvoyage.composeapp.generated.resources.support_me_on_kofi_badge_beige

@OptIn(ExperimentalComposeUiApi::class)
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
                        text = getTranslation(key = "website"),
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
            when (storeState.currentContent) {
                Content.MAIN_MENU -> MainMenuContent(store = store)
                Content.LEARN_MENU -> LearnContent(store = store)
                Content.HOST_TYPES -> HostsContent(store = store)
                Content.PLANET_TYPES -> PlanetsContent(store = store)
                Content.PROPERTIES -> PropertiesContent(store = store)
                Content.MECHANICS -> MechanicsContent(store = store)
                Content.HABITABILITY -> HabitabilityContent(store = store)
            }
        }
    }
}
