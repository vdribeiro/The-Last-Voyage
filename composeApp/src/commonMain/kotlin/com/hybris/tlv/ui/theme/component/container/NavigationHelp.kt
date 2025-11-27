package com.hybris.tlv.ui.theme.component.container

import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hybris.tlv.platform.isAndroid
import com.hybris.tlv.platform.isDesktop
import com.hybris.tlv.platform.isIos
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.component.card.PropertyCard
import com.hybris.tlv.ui.theme.component.list.LazyColumn
import com.hybris.tlv.usecase.translation.TranslationCache
import com.hybris.tlv.usecase.translation.model.Translation

@Composable
internal fun NavigationHelp(
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(space = 8.dp),
    ) {
        item {
            PropertyCard(
                name = "main_menu_screen__navigation",
                description = when {
                    isDesktop -> "main_menu_screen__navigation_info_desktop"
                    isIos || isAndroid -> "main_menu_screen__navigation_info_mobile"
                    else -> "main_menu_screen__navigation_info"
                }
            )
        }
    }
}

@Preview
@Composable
private fun MainMenuPreview() = AppTheme {
    TranslationCache.set(
        translations = listOf(
            Translation(
                key = "main_menu_screen__navigation",
                value = "Navigation"
            ),
            Translation(
                key = "main_menu_screen__navigation_info_mobile",
                value = "Description"
            ),
        )
    )
    NavigationHelp()
}
