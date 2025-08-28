package com.hybris.tlv.usecase.space.local

import com.hybris.tlv.usecase.space.model.Formula
import com.hybris.tlv.usecase.space.model.Planet
import com.hybris.tlv.usecase.space.model.StellarHost

internal interface SpaceLocal {

    /**
     * Returns true if there are no [StellarHost]s in the database, false otherwise.
     */
    fun isStellarHostEmpty(): Boolean

    /**
     * Returns true if there are no [Planet]s in the database, false otherwise.
     */
    fun isPlanetEmpty(): Boolean

    /**
     * Rewrites the [StellarHost] table with the given [stellarHosts].
     */
    fun rewriteStellarHosts(stellarHosts: List<StellarHost>)

    /**
     * Rewrites the [Planet] table with the given [planets].
     */
    fun rewritePlanets(planets: List<Planet>)

    /**
     * Get [StellarHost]s from the database.
     */
    fun getStellarHosts(): List<StellarHost>

    /**
     * Get [StellarHost] by [id] from the database.
     */
    fun getStellarHost(id: String): StellarHost?

    /**
     * Get [Planet]s from the database.
     */
    fun getPlanets(): List<Planet>

    /**
     * Get [Planet]s by [stellarHostId] from the database.
     */
    fun getPlanetsByStellarHost(stellarHostId: String): List<Planet>

    /**
     * Upsert [Formula] in the database.
     */
    fun upsertFormula(formula: Formula)
}
