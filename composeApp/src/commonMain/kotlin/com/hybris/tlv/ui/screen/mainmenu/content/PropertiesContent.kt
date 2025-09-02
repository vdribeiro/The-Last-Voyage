package com.hybris.tlv.ui.screen.mainmenu.content

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.hybris.tlv.ui.component.Section
import com.hybris.tlv.ui.screen.mainmenu.MainMenuAction
import com.hybris.tlv.ui.screen.mainmenu.MainMenuState
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.translation.getTranslation

@Composable
internal fun PropertiesContent(store: Store<MainMenuAction, MainMenuState>) {
    val storeState by store.stateFlow.collectAsState()

    Section(title = getTranslation(key = "main_menu_screen__properties"), sections = properties)
}

private val properties by lazy {
    listOf(
        Section(
            title = getTranslation(key = "main_menu_screen__properties_stellar_host_title"),
            description = getTranslation(key = "main_menu_screen__properties_stellar_host_description")
        ),
        Section(
            title = getTranslation(key = "main_menu_screen__properties_planet_title"),
            description = getTranslation(key = "main_menu_screen__properties_planet_description")
        ),
    )
}
