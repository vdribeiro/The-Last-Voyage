package com.hybris.tlv.ui.screen.mainmenu.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.screen.mainmenu.MainMenuAction
import com.hybris.tlv.ui.screen.mainmenu.MainMenuState
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.ui.theme.component.SimpleCard
import com.hybris.tlv.ui.theme.component.StellarHostCard
import com.hybris.tlv.usecase.learning.model.LearningType
import com.hybris.tlv.usecase.space.mapper.spectralTypeToDrawable
import com.hybris.tlv.usecase.space.model.Planet
import com.hybris.tlv.usecase.space.model.PlanetStatus
import com.hybris.tlv.usecase.space.model.StellarHost
import com.hybris.tlv.usecase.translation.getTranslation

@Composable
internal fun HostDefinitionContent(store: Store<MainMenuAction, MainMenuState>) {
    val storeState by store.stateFlow.collectAsState()
    val stellarHostProperties = storeState.learningsMap[LearningType.HOST_PROPERTY].orEmpty()
    val stellarHosts = storeState.learningsMap[LearningType.HOST_TYPE].orEmpty()
    val stellarHost = remember {
        StellarHost(
            id = "Valar",
            name = "Valar",
            systemName = "Arda",
            spectralType = "G3V",
            effectiveTemperature = 5678.0,
            radius = 1.0,
            mass = 7.0,
            metallicity = 3.0,
            luminosity = 9.0,
            gravity = 2.0,
            age = 1.2,
            density = 2.1,
            rotationalVelocity = 10.0,
            rotationalPeriod = 50.0,
            distance = 9000.0,
            ra = 901.2,
            dec = 345.6,
        ).apply {
            planets.add(
                element = Planet(
                    id = "ME",
                    name = "ME",
                    stellarHostId = "Valar",
                    status = PlanetStatus.FALSE,
                    orbitalPeriod = null,
                    orbitAxis = null,
                    radius = null,
                    mass = null,
                    density = null,
                    eccentricity = null,
                    insolationFlux = null,
                    equilibriumTemperature = null,
                    occultationDepth = null,
                    inclination = null,
                    obliquity = null,
                )
            )
        }
    }
    val exampleTranslation = remember { getTranslation(key = "main_menu_screen__definition_example") }
    val propertiesTranslation = remember { getTranslation(key = "main_menu_screen__definition_properties") }
    val typesTranslation = remember { getTranslation(key = "main_menu_screen__definition_types") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(space = 8.dp),
    ) {
        item {
            Text(
                text = exampleTranslation,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(height = 4.dp))
        }
        item {
            StellarHostCard(
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
        item {
            Text(
                text = propertiesTranslation,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(height = 4.dp))
        }
        items(items = stellarHostProperties, key = { it.id }) { property ->
            SimpleCard(
                name = property.id,
                description = property.description,
            )
        }
        item {
            Text(
                text = typesTranslation,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(height = 4.dp))
        }
        items(items = stellarHosts, key = { it.id }) { stellarHost ->
            StellarHostCard(
                name = getTranslation(key = stellarHost.id),
                description = stellarHost.description,
                spectralTypeDrawable = stellarHost.image.spectralTypeToDrawable(),
            )
        }
    }
}
