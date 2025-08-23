package com.hybris.tlv.usecase.space

import com.hybris.tlv.usecase.space.model.Formula
import com.hybris.tlv.usecase.space.model.StellarHost

internal interface SpaceUseCases {

    /**
     * Upsert [Formula] in the database.
     */
    suspend fun upsertFormula(formula: Formula)

    /**
     * Get exoplanets from the database ordered by ascending distance.
     */
    suspend fun getExoplanets(): List<StellarHost>

    /**
     * Get the nearest [n] stellar hosts of the given [stellarHost] in the [stellarHosts] list and exclude the [visited].
     */
    suspend fun getNearestStars(
        stellarHost: StellarHost,
        stellarHosts: List<StellarHost>,
        n: Int,
        visited: Set<String>
    ): List<StellarHost>
}
