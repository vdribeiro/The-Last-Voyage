package com.hybris.tlv.ui.screen.mainmenu.content

import androidx.compose.runtime.Composable
import com.hybris.tlv.ui.component.Section
import com.hybris.tlv.ui.screen.mainmenu.MainMenuAction
import com.hybris.tlv.ui.screen.mainmenu.MainMenuState
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.translation.getTranslation

@Composable
internal fun MechanicsContent(store: Store<MainMenuAction, MainMenuState>) {
    Section(title = getTranslation(key = "main_menu_screen__mechanics"), sections = mechanics)
}

private val mechanics by lazy {
    listOf(
        Section(
            title = getTranslation(key = "main_menu_screen__mechanics_goal_title"),
            description = getTranslation(key = "main_menu_screen__mechanics_goal_description")
        ),
        Section(
            title = getTranslation(key = "main_menu_screen__mechanics_attributes_title"),
            description = getTranslation(key = "main_menu_screen__mechanics_attributes_description")
        ),
        Section(
            title = getTranslation(key = "main_menu_screen__mechanics_travel_title"),
            description = getTranslation(key = "main_menu_screen__mechanics_travel_description")
        ),
        Section(
            title = getTranslation(key = "main_menu_screen__mechanics_game_over_title"),
            description = getTranslation(key = "main_menu_screen__mechanics_game_over_description")
        ),
        Section(
            title = getTranslation(key = "main_menu_screen__mechanics_score_title"),
            description = getTranslation(key = "main_menu_screen__mechanics_score_description")
        )
    )
}
