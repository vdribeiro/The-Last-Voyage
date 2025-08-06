package com.hybris.tlv.ui.screen.newgame.content

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.hybris.tlv.ui.component.LabeledTextField
import com.hybris.tlv.ui.screen.newgame.NewGameAction
import com.hybris.tlv.ui.screen.newgame.NewGameState
import com.hybris.tlv.ui.store.Store
import com.hybris.tlv.usecase.exoplanet.model.Params
import com.hybris.tlv.usecase.translation.getTranslation

@Composable
internal fun AdvancedContent(store: Store<NewGameAction, NewGameState>) {
    val storeState by store.stateFlow.collectAsState()

    var habitableZoneWeight by remember { mutableStateOf(value = storeState.math.habitableZoneWeight.toString()) }
    var planetRadiusWeight by remember { mutableStateOf(value = storeState.math.planetRadiusWeight.toString()) }
    var planetMassWeight by remember { mutableStateOf(value = storeState.math.planetMassWeight.toString()) }
    var planetTelluricityWeight by remember { mutableStateOf(value = storeState.math.planetTelluricityWeight.toString()) }
    var planetEccentricityWeight by remember { mutableStateOf(value = storeState.math.planetEccentricityWeight.toString()) }
    var planetTemperatureWeight by remember { mutableStateOf(value = storeState.math.planetTemperatureWeight.toString()) }
    var planetObliquityWeight by remember { mutableStateOf(value = storeState.math.planetObliquityWeight.toString()) }
    var planetEsiWeight by remember { mutableStateOf(value = storeState.math.planetEsiWeight.toString()) }
    var stellarSpectralTypeWeight by remember { mutableStateOf(value = storeState.math.stellarSpectralTypeWeight.toString()) }
    var stellarMassWeight by remember { mutableStateOf(value = storeState.math.stellarMassWeight.toString()) }
    var stellarAgeWeight by remember { mutableStateOf(value = storeState.math.stellarAgeWeight.toString()) }
    var stellarActivityWeight by remember { mutableStateOf(value = storeState.math.stellarActivityWeight.toString()) }
    var stellarRotationalPeriodWeight by remember { mutableStateOf(value = storeState.math.stellarRotationalPeriodWeight.toString()) }
    var stellarGravityWeight by remember { mutableStateOf(value = storeState.math.stellarGravityWeight.toString()) }
    var stellarMetallicityWeight by remember { mutableStateOf(value = storeState.math.stellarMetallicityWeight.toString()) }
    var stellarEffectiveTemperatureWeight by remember { mutableStateOf(value = storeState.math.stellarEffectiveTemperatureWeight.toString()) }
    var planetProtectionWeight by remember { mutableStateOf(value = storeState.math.planetProtectionWeight.toString()) }
    var planetTidalLockingWeight by remember { mutableStateOf(value = storeState.math.planetTidalLockingWeight.toString()) }
    var planetMassLowerLimit by remember { mutableStateOf(value = storeState.math.planetMassLowerLimit.toString()) }
    var planetMassIdealUpperLimit by remember { mutableStateOf(value = storeState.math.planetMassIdealUpperLimit.toString()) }
    var planetMassMaxUpperLimit by remember { mutableStateOf(value = storeState.math.planetMassMaxUpperLimit.toString()) }
    var planetRadiusLowerLimit by remember { mutableStateOf(value = storeState.math.planetRadiusLowerLimit.toString()) }
    var planetRadiusIdealUpperLimit by remember { mutableStateOf(value = storeState.math.planetRadiusIdealUpperLimit.toString()) }
    var planetRadiusMaxUpperLimit by remember { mutableStateOf(value = storeState.math.planetRadiusMaxUpperLimit.toString()) }
    var stellarHostEffectiveTemperatureMaxDeviation by remember { mutableStateOf(value = storeState.math.stellarHostEffectiveTemperatureMaxDeviation.toString()) }

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .weight(weight = 1f)
                .padding(all = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item {
                LabeledTextField(
                    label = getTranslation(key = "habitability_habitable_zone_weight"),
                    value = habitableZoneWeight,
                    onValueChange = { newValue ->
                        if (newValue.isEmpty() || newValue.toDoubleOrNull()?.let { it in 0.0..9999.0 } == true) {
                            habitableZoneWeight = newValue
                        }
                    }
                )
            }
            item {
                LabeledTextField(
                    label = getTranslation(key = "habitability_planet_radius_weight"),
                    value = planetRadiusWeight,
                    onValueChange = { newValue ->
                        if (newValue.isEmpty() || newValue.toDoubleOrNull()?.let { it in 0.0..9999.0 } == true) {
                            planetRadiusWeight = newValue
                        }
                    }
                )
            }
            item {
                LabeledTextField(
                    label = getTranslation(key = "habitability_planet_mass_weight"),
                    value = planetMassWeight,
                    onValueChange = { newValue ->
                        if (newValue.isEmpty() || newValue.toDoubleOrNull()?.let { it in 0.0..9999.0 } == true) {
                            planetMassWeight = newValue
                        }
                    }
                )
            }
            item {
                LabeledTextField(
                    label = getTranslation(key = "habitability_planet_telluricity_weight"),
                    value = planetTelluricityWeight,
                    onValueChange = { newValue ->
                        if (newValue.isEmpty() || newValue.toDoubleOrNull()?.let { it in 0.0..9999.0 } == true) {
                            planetTelluricityWeight = newValue
                        }
                    }
                )
            }
            item {
                LabeledTextField(
                    label = getTranslation(key = "habitability_planet_eccentricity_weight"),
                    value = planetEccentricityWeight,
                    onValueChange = { newValue ->
                        if (newValue.isEmpty() || newValue.toDoubleOrNull()?.let { it in 0.0..9999.0 } == true) {
                            planetEccentricityWeight = newValue
                        }
                    }
                )
            }
            item {
                LabeledTextField(
                    label = getTranslation(key = "habitability_planet_temperature_weight"),
                    value = planetTemperatureWeight,
                    onValueChange = { newValue ->
                        if (newValue.isEmpty() || newValue.toDoubleOrNull()?.let { it in 0.0..9999.0 } == true) {
                            planetTemperatureWeight = newValue
                        }
                    }
                )
            }
            item {
                LabeledTextField(
                    label = getTranslation(key = "habitability_planet_obliquity_weight"),
                    value = planetObliquityWeight,
                    onValueChange = { newValue ->
                        if (newValue.isEmpty() || newValue.toDoubleOrNull()?.let { it in 0.0..9999.0 } == true) {
                            planetObliquityWeight = newValue
                        }
                    }
                )
            }
            item {
                LabeledTextField(
                    label = getTranslation(key = "habitability_planet_esi_weight"),
                    value = planetEsiWeight,
                    onValueChange = { newValue ->
                        if (newValue.isEmpty() || newValue.toDoubleOrNull()?.let { it in 0.0..9999.0 } == true) {
                            planetEsiWeight = newValue
                        }
                    }
                )
            }
            item {
                LabeledTextField(
                    label = getTranslation(key = "habitability_stellar_spectral_type_weight"),
                    value = stellarSpectralTypeWeight,
                    onValueChange = { newValue ->
                        if (newValue.isEmpty() || newValue.toDoubleOrNull()?.let { it in 0.0..9999.0 } == true) {
                            stellarSpectralTypeWeight = newValue
                        }
                    }
                )
            }
            item {
                LabeledTextField(
                    label = getTranslation(key = "habitability_stellar_mass_weight"),
                    value = stellarMassWeight,
                    onValueChange = { newValue ->
                        if (newValue.isEmpty() || newValue.toDoubleOrNull()?.let { it in 0.0..9999.0 } == true) {
                            stellarMassWeight = newValue
                        }
                    }
                )
            }
            item {
                LabeledTextField(
                    label = getTranslation(key = "habitability_stellar_age_weight"),
                    value = stellarAgeWeight,
                    onValueChange = { newValue ->
                        if (newValue.isEmpty() || newValue.toDoubleOrNull()?.let { it in 0.0..9999.0 } == true) {
                            stellarAgeWeight = newValue
                        }
                    }
                )
            }
            item {
                LabeledTextField(
                    label = getTranslation(key = "habitability_stellar_activity_weight"),
                    value = stellarActivityWeight,
                    onValueChange = { newValue ->
                        if (newValue.isEmpty() || newValue.toDoubleOrNull()?.let { it in 0.0..9999.0 } == true) {
                            stellarActivityWeight = newValue
                        }
                    }
                )
            }
            item {
                LabeledTextField(
                    label = getTranslation(key = "habitability_stellar_rotational_period_weight"),
                    value = stellarRotationalPeriodWeight,
                    onValueChange = { newValue ->
                        if (newValue.isEmpty() || newValue.toDoubleOrNull()?.let { it in 0.0..9999.0 } == true) {
                            stellarRotationalPeriodWeight = newValue
                        }
                    }
                )
            }
            item {
                LabeledTextField(
                    label = getTranslation(key = "habitability_stellar_gravity_weight"),
                    value = stellarGravityWeight,
                    onValueChange = { newValue ->
                        if (newValue.isEmpty() || newValue.toDoubleOrNull()?.let { it in 0.0..9999.0 } == true) {
                            stellarGravityWeight = newValue
                        }
                    }
                )
            }
            item {
                LabeledTextField(
                    label = getTranslation(key = "habitability_stellar_metallicity_weight"),
                    value = stellarMetallicityWeight,
                    onValueChange = { newValue ->
                        if (newValue.isEmpty() || newValue.toDoubleOrNull()?.let { it in 0.0..9999.0 } == true) {
                            stellarMetallicityWeight = newValue
                        }
                    }
                )
            }
            item {
                LabeledTextField(
                    label = getTranslation(key = "habitability_stellar_effective_temperature_weight"),
                    value = stellarEffectiveTemperatureWeight,
                    onValueChange = { newValue ->
                        if (newValue.isEmpty() || newValue.toDoubleOrNull()?.let { it in 0.0..9999.0 } == true) {
                            stellarEffectiveTemperatureWeight = newValue
                        }
                    }
                )
            }
            item {
                LabeledTextField(
                    label = getTranslation(key = "habitability_planet_protection_weight"),
                    value = planetProtectionWeight,
                    onValueChange = { newValue ->
                        if (newValue.isEmpty() || newValue.toDoubleOrNull()?.let { it in 0.0..9999.0 } == true) {
                            planetProtectionWeight = newValue
                        }
                    }
                )
            }
            item {
                LabeledTextField(
                    label = getTranslation(key = "habitability_planet_tidal_locking_weight"),
                    value = planetTidalLockingWeight,
                    onValueChange = { newValue ->
                        if (newValue.isEmpty() || newValue.toDoubleOrNull()?.let { it in 0.0..9999.0 } == true) {
                            planetTidalLockingWeight = newValue
                        }
                    }
                )
            }
            item {
                LabeledTextField(
                    label = getTranslation(key = "habitability_planet_mass_lower_limit"),
                    value = planetMassLowerLimit,
                    onValueChange = { newValue ->
                        if (newValue.isEmpty() || newValue.toDoubleOrNull()?.let { it in 0.0..9999.0 } == true) {
                            planetMassLowerLimit = newValue
                        }
                    }
                )
            }
            item {
                LabeledTextField(
                    label = getTranslation(key = "habitability_planet_mass_upper_limit"),
                    value = planetMassIdealUpperLimit,
                    onValueChange = { newValue ->
                        if (newValue.isEmpty() || newValue.toDoubleOrNull()?.let { it in 0.0..9999.0 } == true) {
                            planetMassIdealUpperLimit = newValue
                        }
                    }
                )
            }
            item {
                LabeledTextField(
                    label = getTranslation(key = "habitability_planet_mass_max_upper_limit"),
                    value = planetMassMaxUpperLimit,
                    onValueChange = { newValue ->
                        if (newValue.isEmpty() || newValue.toDoubleOrNull()?.let { it in 0.0..9999.0 } == true) {
                            planetMassMaxUpperLimit = newValue
                        }
                    }
                )
            }
            item {
                LabeledTextField(
                    label = getTranslation(key = "habitability_planet_radius_lower_limit"),
                    value = planetRadiusLowerLimit,
                    onValueChange = { newValue ->
                        if (newValue.isEmpty() || newValue.toDoubleOrNull()?.let { it in 0.0..9999.0 } == true) {
                            planetRadiusLowerLimit = newValue
                        }
                    }
                )
            }
            item {
                LabeledTextField(
                    label = getTranslation(key = "habitability_planet_radius_upper_limit"),
                    value = planetRadiusIdealUpperLimit,
                    onValueChange = { newValue ->
                        if (newValue.isEmpty() || newValue.toDoubleOrNull()?.let { it in 0.0..9999.0 } == true) {
                            planetRadiusIdealUpperLimit = newValue
                        }
                    }
                )
            }
            item {
                LabeledTextField(
                    label = getTranslation(key = "habitability_planet_radius_max_upper_limit"),
                    value = planetRadiusMaxUpperLimit,
                    onValueChange = { newValue ->
                        if (newValue.isEmpty() || newValue.toDoubleOrNull()?.let { it in 0.0..9999.0 } == true) {
                            planetRadiusMaxUpperLimit = newValue
                        }
                    }
                )
            }
            item {
                LabeledTextField(
                    label = getTranslation(key = "habitability_stellar_host_effective_temperature_max_deviation"),
                    value = stellarHostEffectiveTemperatureMaxDeviation,
                    onValueChange = { newValue ->
                        if (newValue.isEmpty() || newValue.toDoubleOrNull()?.let { it in 0.0..9999.0 } == true) {
                            stellarHostEffectiveTemperatureMaxDeviation = newValue
                        }
                    }
                )
            }
        }
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 0.dp,
                    bottom = 32.dp,
                    start = 16.dp,
                    end = 16.dp
                ),
            colors = ButtonDefaults.buttonColors(contentColor = Color.White),
            onClick = {
                store.send(
                    action = NewGameAction.SelectMath(
                        math = Params.Math(
                            habitableZoneWeight = habitableZoneWeight.toDouble(),
                            planetRadiusWeight = planetRadiusWeight.toDouble(),
                            planetMassWeight = planetMassWeight.toDouble(),
                            planetTelluricityWeight = planetTelluricityWeight.toDouble(),
                            planetEccentricityWeight = planetEccentricityWeight.toDouble(),
                            planetTemperatureWeight = planetTemperatureWeight.toDouble(),
                            planetObliquityWeight = planetObliquityWeight.toDouble(),
                            planetEsiWeight = planetEsiWeight.toDouble(),
                            stellarSpectralTypeWeight = stellarSpectralTypeWeight.toDouble(),
                            stellarMassWeight = stellarMassWeight.toDouble(),
                            stellarAgeWeight = stellarAgeWeight.toDouble(),
                            stellarActivityWeight = stellarActivityWeight.toDouble(),
                            stellarRotationalPeriodWeight = stellarRotationalPeriodWeight.toDouble(),
                            stellarGravityWeight = stellarGravityWeight.toDouble(),
                            stellarMetallicityWeight = stellarMetallicityWeight.toDouble(),
                            stellarEffectiveTemperatureWeight = stellarEffectiveTemperatureWeight.toDouble(),
                            planetProtectionWeight = planetProtectionWeight.toDouble(),
                            planetTidalLockingWeight = planetTidalLockingWeight.toDouble(),
                            planetMassLowerLimit = planetMassLowerLimit.toDouble(),
                            planetMassIdealUpperLimit = planetMassIdealUpperLimit.toDouble(),
                            planetMassMaxUpperLimit = planetMassMaxUpperLimit.toDouble(),
                            planetRadiusLowerLimit = planetRadiusLowerLimit.toDouble(),
                            planetRadiusIdealUpperLimit = planetRadiusIdealUpperLimit.toDouble(),
                            planetRadiusMaxUpperLimit = planetRadiusMaxUpperLimit.toDouble(),
                            stellarHostEffectiveTemperatureMaxDeviation = stellarHostEffectiveTemperatureMaxDeviation.toDouble()
                        )
                    )
                )
                store.send(action = NewGameAction.Ship)
            }
        ) {
            Text(text = getTranslation(key = "new_game_screen__continue"))
        }
    }
}
