package com.hybris.tlv.usecase.space.model

enum class PlanetStatus(val displayName: String) {
    CONFIRMED(displayName = "planet_status_confirmed"),
    CANDIDATE(displayName = "planet_status_candidate"),
    FALSE(displayName = "planet_status_false")
}
