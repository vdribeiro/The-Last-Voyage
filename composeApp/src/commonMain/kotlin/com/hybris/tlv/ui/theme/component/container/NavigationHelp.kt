package com.hybris.tlv.ui.theme.component.container

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hybris.tlv.platform.isAndroid
import com.hybris.tlv.platform.isDesktop
import com.hybris.tlv.platform.isIos
import com.hybris.tlv.ui.theme.component.card.PropertyCard
import com.hybris.tlv.ui.theme.component.list.LazyColumn
import com.hybris.tlv.usecase.translation.TranslationCache
import com.hybris.tlv.usecase.translation.getTranslation

@Composable
internal fun NavigationHelp(
    modifier: Modifier = Modifier,
) {
    val translationVersion by TranslationCache.stateFlow.collectAsState()
    val navigationTranslation = remember(key1 = translationVersion) { getTranslation(key = "main_menu_screen__navigation") }
    val navigationDescriptionTranslation = remember(key1 = translationVersion) {
        getTranslation(
            key = when {
                isDesktop -> "main_menu_screen__navigation_info_desktop"
                isIos || isAndroid -> "main_menu_screen__navigation_info_mobile"
                else -> "main_menu_screen__navigation_info"
            }
        )
    }
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(space = 8.dp),
    ) {
        item {
            PropertyCard(
                name = navigationTranslation,
                description = navigationDescriptionTranslation,
            )
        }
    }
}