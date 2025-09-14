package com.hybris.tlv.usecase.space.model

internal sealed interface ExoplanetsResult {
    data class Success(val stellarHosts: List<StellarHost>, val planets: List<Planet>): ExoplanetsResult
    data class Error(val error: String): ExoplanetsResult
}
