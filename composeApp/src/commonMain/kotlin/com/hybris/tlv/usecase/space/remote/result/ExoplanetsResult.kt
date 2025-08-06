package com.hybris.tlv.usecase.space.remote.result

import com.hybris.tlv.usecase.space.model.Planet
import com.hybris.tlv.usecase.space.model.StellarHost

internal sealed interface ExoplanetsResult {
    data class Success(val stellarHosts: List<StellarHost>, val planets: List<Planet>): ExoplanetsResult
    data class Error(val error: String): ExoplanetsResult
}
