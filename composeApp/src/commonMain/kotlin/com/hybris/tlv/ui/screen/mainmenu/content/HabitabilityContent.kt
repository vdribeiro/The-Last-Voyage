package com.hybris.tlv.ui.screen.mainmenu.content

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.screen.mainmenu.MAIN_MENU_SCREEN_HABITABILITY_CONTENT
import com.hybris.tlv.ui.screen.mainmenu.MAIN_MENU_SCREEN_HABITABILITY_CONTENT_FORMULA
import com.hybris.tlv.ui.screen.mainmenu.MAIN_MENU_SCREEN_HABITABILITY_CONTENT_SIMPLE
import com.hybris.tlv.ui.screen.mainmenu.MainMenuAction
import com.hybris.tlv.ui.screen.mainmenu.MainMenuState
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.ui.theme.LocalColorScheme
import com.hybris.tlv.ui.theme.LocalTypography
import com.hybris.tlv.ui.theme.component.SimpleCard
import com.hybris.tlv.usecase.learning.model.LearningType
import com.hybris.tlv.usecase.translation.getTranslation

@Composable
internal fun HabitabilityContent(store: Store<MainMenuAction, MainMenuState>) {
    val storeState by store.stateFlow.collectAsState()
    val formula = storeState.learningsMap[LearningType.FORMULA].orEmpty()
    val uriHandler = LocalUriHandler.current
    val formulaTranslation = remember { getTranslation(key = "formula") }

    val typography = LocalTypography.current
    val colorScheme = LocalColorScheme.current

    LazyColumn(
        modifier = Modifier
            .testTag(tag = MAIN_MENU_SCREEN_HABITABILITY_CONTENT)
            .fillMaxSize()
            .padding(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(space = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        items(items = formula, key = { it.id }) { property ->
            SimpleCard(
                modifier = Modifier.testTag(tag = MAIN_MENU_SCREEN_HABITABILITY_CONTENT_SIMPLE),
                name = property.id,
                description = property.description,
            )
        }
        item {
            Text(
                modifier = Modifier
                    .testTag(tag = MAIN_MENU_SCREEN_HABITABILITY_CONTENT_FORMULA)
                    .clickable { uriHandler.openUri(uri = storeState.formula) },
                text = formulaTranslation,
                style = typography.bodyLarge.copy(
                    color = colorScheme.primary,
                    textDecoration = TextDecoration.Underline
                ),
            )
        }
    }
}
