package com.hybris.tlv.usecase.space.model

import com.hybris.tlv.usecase.translation.getTranslation
import kotlinx.serialization.Serializable

@Serializable
internal data class TravelOutcome(
    val integrity: Int? = null,
    val materials: Int? = null,
    val fuel: Int? = null,
    val cryopods: Int? = null,
) {
    fun getTranslation(): String = buildList {
        if (integrity != null) add("${if (integrity > 0) "+" else ""}$integrity ${getTranslation(key = "ship_integrity")}")
        if (materials != null) add("${if (materials > 0) "+" else ""}$materials ${getTranslation(key = "ship_materials")}")
        if (fuel != null) add("${if (fuel > 0.0) "+" else ""}$fuel ${getTranslation(key = "ship_fuel")}")
        if (cryopods != null) add("${if (cryopods > 0) "+" else ""}$cryopods ${getTranslation(key = "ship_cryopods")}")
    }.joinToString(separator = "\n")
}
