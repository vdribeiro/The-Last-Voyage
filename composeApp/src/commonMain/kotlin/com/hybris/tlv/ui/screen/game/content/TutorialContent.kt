package com.hybris.tlv.ui.screen.game.content

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.hybris.tlv.ui.screen.game.GameAction
import com.hybris.tlv.ui.screen.game.GameState
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.translation.getTranslation

@Composable
internal fun TutorialContent(store: Store<GameAction, GameState>) {
    remember { getTranslation(key = "ship_years_traveled") }
    remember { getTranslation(key = "ship_sensor") }
    remember { getTranslation(key = "ship_speed") }
    remember { getTranslation(key = "ship_integrity") }
    remember { getTranslation(key = "ship_fuel") }
    remember { getTranslation(key = "ship_materials") }
    remember { getTranslation(key = "ship_cryopods") }
}
