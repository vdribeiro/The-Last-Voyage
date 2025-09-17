package com.hybris.tlv.ui.screen.mainmenu.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.screen.mainmenu.MAIN_MENU_SCREEN_PLANET_DEFINITION_CONTENT
import com.hybris.tlv.ui.screen.mainmenu.MAIN_MENU_SCREEN_PLANET_DEFINITION_CONTENT_EXAMPLE
import com.hybris.tlv.ui.screen.mainmenu.MAIN_MENU_SCREEN_PLANET_DEFINITION_CONTENT_EXAMPLE_PLANET
import com.hybris.tlv.ui.screen.mainmenu.MAIN_MENU_SCREEN_PLANET_DEFINITION_CONTENT_PROPERTIES
import com.hybris.tlv.ui.screen.mainmenu.MAIN_MENU_SCREEN_PLANET_DEFINITION_CONTENT_PROPERTIES_SIMPLE
import com.hybris.tlv.ui.screen.mainmenu.MAIN_MENU_SCREEN_PLANET_DEFINITION_CONTENT_TYPES
import com.hybris.tlv.ui.screen.mainmenu.MAIN_MENU_SCREEN_PLANET_DEFINITION_CONTENT_TYPES_PLANET
import com.hybris.tlv.ui.screen.mainmenu.MainMenuAction
import com.hybris.tlv.ui.screen.mainmenu.MainMenuState
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.ui.theme.LocalTypography
import com.hybris.tlv.ui.theme.component.PlanetCard
import com.hybris.tlv.ui.theme.component.SimpleCard
import com.hybris.tlv.usecase.learning.model.LearningType
import com.hybris.tlv.usecase.space.formula.toDrawable
import com.hybris.tlv.usecase.space.model.Planet
import com.hybris.tlv.usecase.space.model.PlanetStatus
import com.hybris.tlv.usecase.space.model.PlanetType
import com.hybris.tlv.usecase.space.model.Score
import com.hybris.tlv.usecase.translation.getTranslation

@Composable
internal fun PlanetDefinitionContent(store: Store<MainMenuAction, MainMenuState>) {
    val storeState by store.stateFlow.collectAsState()
    val planetProperties = storeState.learningsMap[LearningType.PLANET_PROPERTY].orEmpty()
    val planets = storeState.learningsMap[LearningType.PLANET_TYPE].orEmpty()
    val planet = remember {
        Planet(
            id = "Edoras",
            name = "Edoras",
            stellarHostId = "Valar",
            status = PlanetStatus.CANDIDATE,
            orbitalPeriod = 123.0,
            orbitAxis = 1.2,
            radius = 5.1,
            mass = 2.3,
            density = 3.2,
            eccentricity = 0.5,
            insolationFlux = 2.1,
            equilibriumTemperature = 666.9,
            occultationDepth = 0.01,
            inclination = 1.8,
            obliquity = 50.0,
        ).apply {
            score = Score(
                habitabilityScore = 1.0,
                confidenceScore = 1.0,
                planetType = PlanetType.SUPERHABITABLE_PLANET,
                rocheScore = null,
                habitableZoneKopparapuScore = null,
                habitableZoneKastingScore = null,
                planetRadiusScore = null,
                planetMassScore = null,
                planetTelluricityScore = null,
                planetEccentricityScore = null,
                planetTemperatureScore = null,
                planetObliquityScore = null,
                planetEsiScore = null,
                stellarSpectralTypeScore = null,
                stellarMassScore = null,
                stellarAgeScore = null,
                stellarActivityScore = null,
                stellarRotationalPeriodScore = null,
                stellarGravityScore = null,
                stellarMetallicityScore = null,
                stellarEffectiveTemperatureScore = null,
                planetProtectionScore = null,
                planetTidalLockingScore = null
            )
        }
    }
    val exampleTranslation = remember { getTranslation(key = "main_menu_screen__definition_example") }
    val propertiesTranslation = remember { getTranslation(key = "main_menu_screen__definition_properties") }
    val typesTranslation = remember { getTranslation(key = "main_menu_screen__definition_types") }

    val typography = LocalTypography.current

    LazyColumn(
        modifier = Modifier
            .testTag(tag = MAIN_MENU_SCREEN_PLANET_DEFINITION_CONTENT)
            .fillMaxSize()
            .padding(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(space = 8.dp),
    ) {
        item {
            Text(
                modifier = Modifier
                    .testTag(tag = MAIN_MENU_SCREEN_PLANET_DEFINITION_CONTENT_EXAMPLE),
                text = exampleTranslation,
                style = typography.titleLarge,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(height = 4.dp))
        }
        item {
            PlanetCard(
                modifier = Modifier
                    .testTag(tag = MAIN_MENU_SCREEN_PLANET_DEFINITION_CONTENT_EXAMPLE_PLANET),
                name = planet.name,
                status = planet.status.displayName,
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
                type = planet.score?.planetType?.displayName,
                typeDrawable = planet.score?.planetType.toDrawable()
            )
        }
        item {
            Text(
                modifier = Modifier
                    .testTag(tag = MAIN_MENU_SCREEN_PLANET_DEFINITION_CONTENT_PROPERTIES),
                text = propertiesTranslation,
                style = typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(height = 4.dp))
        }
        items(items = planetProperties, key = { it.id }) { property ->
            SimpleCard(
                modifier = Modifier
                    .testTag(tag = MAIN_MENU_SCREEN_PLANET_DEFINITION_CONTENT_PROPERTIES_SIMPLE),
                name = property.id,
                description = property.description,
            )
        }
        item {
            Text(
                modifier = Modifier
                    .testTag(tag = MAIN_MENU_SCREEN_PLANET_DEFINITION_CONTENT_TYPES),
                text = typesTranslation,
                style = typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(height = 4.dp))
        }
        items(items = planets, key = { it.id }) { planet ->
            PlanetCard(
                modifier = Modifier
                    .testTag(tag = MAIN_MENU_SCREEN_PLANET_DEFINITION_CONTENT_TYPES_PLANET),
                name = getTranslation(key = planet.id),
                description = planet.description,
                typeDrawable = PlanetType.fromValue(value = planet.image.orEmpty()).toDrawable()
            )
        }
    }
}
