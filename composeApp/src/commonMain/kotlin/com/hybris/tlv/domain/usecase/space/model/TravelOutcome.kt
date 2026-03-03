package com.hybris.tlv.domain.usecase.space.model

import kotlinx.serialization.Serializable
import com.hybris.tlv.domain.usecase.translation.TranslationCache

@Serializable
internal data class TravelOutcome(
    val integrity: Int? = null,
    val materials: Int? = null,
    val fuel: Int? = null,
    val cryopods: Int? = null,
) {
    fun toStringOutcome() = buildList {
        if (integrity != null) add("${if (integrity > 0) "+" else ""}$integrity ${TranslationCache.get(key = "ship_integrity")}")
        if (materials != null) add("${if (materials > 0) "+" else ""}$materials ${TranslationCache.get(key = "ship_materials")}")
        if (fuel != null) add("${if (fuel > 0.0) "+" else ""}$fuel ${TranslationCache.get(key = "ship_fuel")}")
        if (cryopods != null) add("${if (cryopods > 0) "+" else ""}$cryopods ${TranslationCache.get(key = "ship_cryopods")}")
    }.joinToString(separator = "\n")
}
