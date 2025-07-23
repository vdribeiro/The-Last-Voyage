package com.hybris.tlv.ui.screen.explore

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.screen.explore.content.MenuContent
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.translation.getTranslation

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun ExploreScreen(store: Store<ExploreAction, ExploreState>) {
    val storeState by store.stateFlow.collectAsState()
    val currentContent = storeState.currentContent ?: return

    BackHandler(enabled = true) { store.send(action = ExploreAction.Back) }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Box(modifier = Modifier.padding(paddingValues = innerPadding)) {
            when (currentContent) {
                Content.MENU -> MenuContent(store = store)
                Content.MECHANICS -> Section(title = "explore_screen__mechanics", sections = mechanics)
                Content.HABITABILITY -> Section(title = "explore_screen__habitability", sections = habitabilities)
            }
        }
    }
}

private val mechanics = listOf(
    Section(
        title = "explore_screen__mechanics_goal_title",
        description = "explore_screen__mechanics_goal_description"
    ),
    Section(
        title = "explore_screen__mechanics_attributes_title",
        description = "explore_screen__mechanics_attributes_description"
    ),
    Section(
        title = "explore_screen__mechanics_travel_title",
        description = "explore_screen__mechanics_travel_description"
    ),
    Section(
        title = "explore_screen__mechanics_game_over_title",
        description = "explore_screen__mechanics_game_over_description"
    ),
    Section(
        title = "explore_screen__mechanics_score_title",
        description = "explore_screen__mechanics_score_description"
    )
)

private val habitabilities = listOf(
    Section(
        title = "explore_screen__habitability_title",
        description = "explore_screen__habitability_description"
    ),
    Section(
        title = "explore_screen__habitability_hz_title",
        description = "explore_screen__habitability_hz_description"
    ),
    Section(
        title = "explore_screen__habitability_planet_density_radius_title",
        description = "explore_screen__habitability_planet_density_radius_description"
    ),
    Section(
        title = "explore_screen__habitability_planet_mass_title",
        description = "explore_screen__habitability_planet_mass_description"
    ),
    Section(
        title = "explore_screen__habitability_planet_eccentricity_title",
        description = "explore_screen__habitability_planet_eccentricity_description"
    ),
    Section(
        title = "explore_screen__habitability_planet_temperature_title",
        description = "explore_screen__habitability_planet_temperature_description"
    ),
    Section(
        title = "explore_screen__habitability_planet_obliquity_title",
        description = "explore_screen__habitability_planet_obliquity_description"
    ),
    Section(
        title = "explore_screen__habitability_planet_protection_title",
        description = "explore_screen__habitability_planet_protection_description"
    ),
    Section(
        title = "explore_screen__habitability_planet_tidal_locking_title",
        description = "explore_screen__habitability_planet_tidal_locking_description"
    ),
    Section(
        title = "explore_screen__habitability_planet_esi_title",
        description = "explore_screen__habitability_planet_esi_description"
    ),
    Section(
        title = "explore_screen__habitability_star_spectral_type_title",
        description = "explore_screen__habitability_star_spectral_type_description"
    ),
    Section(
        title = "explore_screen__habitability_star_age_title",
        description = "explore_screen__habitability_star_age_description"
    ),
    Section(
        title = "explore_screen__habitability_star_activity_title",
        description = "explore_screen__habitability_star_activity_description"
    ),
    Section(
        title = "explore_screen__habitability_star_gravity_title",
        description = "explore_screen__habitability_star_gravity_description"
    ),
    Section(
        title = "explore_screen__habitability_star_metallicity_title",
        description = "explore_screen__habitability_star_metallicity_description"
    ),
    Section(
        title = "explore_screen__habitability_star_effective_temperature_title",
        description = "explore_screen__habitability_star_effective_temperature_description"
    ),
)

private data class Section(
    val title: String,
    val description: String
)

@Composable
private fun Section(title: String, sections: List<Section>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(space = 24.dp),
    ) {
        item {
            Text(
                text = getTranslation(key = title),
                style = MaterialTheme.typography.headlineLarge,
            )
        }
        items(items = sections, key = { it.title }) { section ->
            Text(
                text = getTranslation(key = section.title),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(height = 8.dp))
            Text(
                text = getTranslation(key = section.description),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
