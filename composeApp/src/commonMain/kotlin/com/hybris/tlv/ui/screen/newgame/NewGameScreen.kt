package com.hybris.tlv.ui.screen.newgame

import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.preview.getStore
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.ui.theme.AppTheme
import com.hybris.tlv.ui.theme.LocalTypography
import com.hybris.tlv.ui.theme.component.AttributePoint
import com.hybris.tlv.ui.theme.component.AttributeRow
import com.hybris.tlv.ui.theme.component.bottombar.ButtonsBar
import com.hybris.tlv.ui.theme.component.card.SelectableAttribute
import com.hybris.tlv.ui.theme.component.container.TypewriterContent
import com.hybris.tlv.ui.theme.component.screen.Screen
import com.hybris.tlv.ui.theme.component.text.Text
import com.hybris.tlv.usecase.catastrophe.model.Catastrophe
import com.hybris.tlv.usecase.ship.model.Engine
import com.hybris.tlv.usecase.ship.model.ShipPrototype
import com.hybris.tlv.usecase.translation.TranslationCache
import com.hybris.tlv.usecase.translation.getTranslation

@Composable
internal fun NewGameScreen(store: Store<NewGameState, NewGameAction>) {
    val storeState by store.stateFlow.collectAsState()
    val translationVersion by TranslationCache.updateFlow.collectAsState()
    val continueTranslation = remember(key1 = translationVersion) { getTranslation(key = "new_game_screen__continue") }
    val startTranslation = remember(key1 = translationVersion) { getTranslation(key = "new_game_screen__start") }

    Screen(
        modifier = Modifier.testTag(tag = NEW_GAME_SCREEN),
        loading = storeState.loading,
        onMusicClick = { store.toggleAudio() },
        onFeedbackClick = { store.feedback() },
        bottomBar = {
            ButtonsBar(
                buttons = when (storeState.currentContent) {
                    Content.SHIP -> buildList {
                        val shipState = storeState.shipState
                        val onClick = when {
                            shipState?.remainingPoints != null && shipState.remainingPoints >= 0 -> {
                                {
                                    val shipPrototype = ShipPrototype(
                                        assignedPoints = shipState.assignedPoints,
                                        sensorRange = shipState.sensorRange.value,
                                        fuel = shipState.fuel.value,
                                        materials = shipState.materials.value,
                                        cryopods = shipState.cryopods.value,
                                    )
                                    store.send(action = NewGameAction.SelectShip(ship = shipPrototype))
                                }
                            }

                            else -> null
                        }
                        add(element = continueTranslation to onClick)
                    }

                    Content.START -> listOf(startTranslation to { store.send(action = NewGameAction.Next) })
                }
            )
        },
    ) {
        when (storeState.currentContent) {
            Content.SHIP -> Ship(store = store)
            Content.START -> TypewriterContent(
                title = storeState.selectedCatastrophe?.let { getTranslation(key = it.id) },
                text = storeState.selectedCatastrophe?.let { getTranslation(key = it.description) }
            )
        }
    }
}

@Composable
private fun Ship(store: Store<NewGameState, NewGameAction>) {
    val storeState by store.stateFlow.collectAsState()
    val shipState = storeState.shipState ?: return
    val engines = storeState.engines

    val translationVersion by TranslationCache.updateFlow.collectAsState()
    val shipPointsTranslation = remember(key1 = translationVersion) { getTranslation(key = "new_game_screen__ship_points") }
    val sensorTranslation = remember(key1 = translationVersion) { getTranslation(key = "ship_sensor") }
    val fuelTranslation = remember(key1 = translationVersion) { getTranslation(key = "ship_fuel") }
    val materialsTranslation = remember(key1 = translationVersion) { getTranslation(key = "ship_materials") }
    val cryopodsTranslation = remember(key1 = translationVersion) { getTranslation(key = "ship_cryopods") }
    val engineSpeedTranslation = remember(key1 = translationVersion) { getTranslation(key = "new_game_screen__engine_speed") }
    val engineFuelTranslation = remember(key1 = translationVersion) { getTranslation(key = "new_game_screen__engine_fuel") }
    val engineSelectTranslation = remember(key1 = translationVersion) { getTranslation(key = "new_game_screen__engine_select") }

    val typography = LocalTypography.current

    Column(
        modifier = Modifier
            .testTag(tag = NEW_GAME_SCREEN_NEW_GAME_CONTENT)
            .fillMaxSize()
            .padding(all = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Remaining points
        Text(
            modifier = Modifier.testTag(tag = NEW_GAME_SCREEN_NEW_GAME_CONTENT_POINTS_TEXT),
            text = "$shipPointsTranslation: ${shipState.remainingPoints}",
            style = typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(height = 16.dp))

        // Attributes for sensor range, fuel, materials and cryopods
        LazyColumn(
            modifier = Modifier
                .testTag(tag = NEW_GAME_SCREEN_NEW_GAME_CONTENT_POINTS)
                .weight(weight = 1f),
            verticalArrangement = Arrangement.spacedBy(space = 4.dp, alignment = Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val canIncrement = shipState.remainingPoints > 0
            item {
                AttributeRow(
                    name = sensorTranslation,
                    canIncrement = canIncrement,
                    attributePoint = shipState.sensorRange
                )
            }
            item {
                AttributeRow(
                    name = fuelTranslation,
                    canIncrement = canIncrement,
                    attributePoint = shipState.fuel
                )
            }
            item {
                AttributeRow(
                    name = materialsTranslation,
                    canIncrement = canIncrement,
                    attributePoint = shipState.materials
                )
            }
            item {
                AttributeRow(
                    name = cryopodsTranslation,
                    canIncrement = canIncrement,
                    attributePoint = shipState.cryopods
                )
            }
            item {
                Text(
                    modifier = Modifier.padding(all = 16.dp),
                    text = engineSelectTranslation,
                    style = typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
            items(items = engines, key = { it.id }) { engine ->
                SelectableAttribute(
                    modifier = Modifier
                        .testTag(tag = NEW_GAME_SCREEN_NEW_GAME_CONTENT_ENGINE)
                        .clickable { store.send(action = NewGameAction.SelectEngine(engine = engine)) },
                    selected = shipState.engine == engine,
                    name = getTranslation(key = engine.id),
                    description = getTranslation(key = engine.description),
                    velocity = "$engineSpeedTranslation: ${engine.velocity}c",
                    fuel = "$engineFuelTranslation: ${engine.fuelConsumption}",
                    points = "${engine.cost}",
                )
            }
        }
    }
}

@Preview
@Composable
private fun NewGameLoadingPreview() = AppTheme {
    NewGameScreen(
        store = getStore(
            initialState = NewGameState(
                loading = true,
                currentContent = Content.SHIP,
                selectedCatastrophe = null,
                shipState = null,
            )
        )
    )
}

@Preview
@Composable
private fun NewGameShipPreview() = AppTheme {
    NewGameScreen(
        store = getStore(
            initialState = NewGameState(
                loading = false,
                currentContent = Content.SHIP,
                selectedCatastrophe = null,
                shipState = ShipState(
                    totalPoints = 10,
                    sensorRange = AttributePoint(max = 10, min = 1, interval = 1, initialValue = 3),
                    materials = AttributePoint(max = 1000, min = 0, interval = 100, initialValue = 100),
                    fuel = AttributePoint(max = 1000, min = 0, interval = 100, initialValue = 100),
                    cryopods = AttributePoint(max = 1000, min = 0, interval = 100, initialValue = 100),
                    engine = Engine(
                        id = "Engine",
                        description = "Engine description",
                        velocity = 10.0,
                        fuelConsumption = 1.0,
                        cost = 1
                    )
                ),
            )
        )
    )
}

@Preview
@Composable
private fun NewGameStartPreview() = AppTheme {
    NewGameScreen(
        store = getStore(
            initialState = NewGameState(
                loading = false,
                currentContent = Content.START,
                selectedCatastrophe = Catastrophe(
                    id = "Asteroid Impact",
                    description = "A massive asteroid collides with Earth. The impact wipes out most life on the planet.",
                ),
                shipState = null,
            )
        )
    )
}
