package com.hybris.tlv.ui.screen.game.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.screen.game.GAME_SCREEN_SYSTEM_CONTENT
import com.hybris.tlv.ui.screen.game.GAME_SCREEN_SYSTEM_CONTENT_PLANET
import com.hybris.tlv.ui.screen.game.GAME_SCREEN_SYSTEM_CONTENT_STELLAR_HOST
import com.hybris.tlv.ui.screen.game.GameAction
import com.hybris.tlv.ui.screen.game.GameState
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.ui.theme.component.PlanetCard
import com.hybris.tlv.ui.theme.component.StellarHostCard
import com.hybris.tlv.ui.theme.thenIf
import com.hybris.tlv.usecase.space.formula.spectralTypeToDrawable
import com.hybris.tlv.usecase.space.formula.toDrawable

@Composable
internal fun SystemContent(store: Store<GameAction, GameState>) {
    val storeState by store.stateFlow.collectAsState()
    val stellarHost = storeState.currentStellarHost ?: return

    LazyColumn(
        modifier = Modifier.thenIf(
            tag = GAME_SCREEN_SYSTEM_CONTENT,
            maxWidth = Dp.Infinity,
            maxHeight = Dp.Infinity,
            padding = PaddingValues(all = 16.dp)
        ),
        verticalArrangement = Arrangement.spacedBy(space = 8.dp)
    ) {
        item(key = stellarHost.id) {
            StellarHostCard(
                modifier = Modifier.thenIf(tag = GAME_SCREEN_SYSTEM_CONTENT_STELLAR_HOST),
                name = stellarHost.name,
                systemName = stellarHost.systemName,
                planetCount = stellarHost.planets.size,
                spectralType = stellarHost.spectralType,
                spectralTypeDrawable = stellarHost.spectralType.spectralTypeToDrawable(),
                effectiveTemperature = stellarHost.effectiveTemperature,
                radius = stellarHost.radius,
                mass = stellarHost.mass,
                metallicity = stellarHost.metallicity,
                luminosity = stellarHost.luminosity,
                gravity = stellarHost.gravity,
                age = stellarHost.age,
                density = stellarHost.density,
                rotationalVelocity = stellarHost.rotationalVelocity,
                rotationalPeriod = stellarHost.rotationalPeriod,
                distance = stellarHost.distance,
                ra = stellarHost.ra,
                dec = stellarHost.dec,
            )
        }
        item { HorizontalDivider(modifier = Modifier.thenIf(padding = PaddingValues(vertical = 8.dp))) }
        items(items = stellarHost.planets, key = { it.id }) { planet ->
            PlanetCard(
                modifier = Modifier.thenIf(
                    tag = GAME_SCREEN_SYSTEM_CONTENT_PLANET,
                    onClick = { store.send(action = GameAction.Settle(planet = planet)) }
                ),
                name = planet.name,
                orbitalPeriod = planet.orbitalPeriod,
                orbitAxis = planet.orbitAxis,
                radius = planet.radius,
                mass = planet.mass,
                density = planet.density,
                eccentricity = planet.eccentricity,
                insolationFlux = planet.insolationFlux,
                equilibriumTemperature = planet.equilibriumTemperature,
                occultationDepth = planet.occultationDepth,
                inclination = planet.inclination,
                obliquity = planet.obliquity,
                habitability = planet.score?.habitabilityScore,
                type = planet.score?.planetType?.displayName,
                typeDrawable = planet.score?.planetType.toDrawable()
            )
        }
    }
}
