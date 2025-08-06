package com.hybris.tlv.usecase.earth.local

import com.hybris.tlv.usecase.earth.model.Catastrophe

internal interface EarthLocal {

    /**
     * Returns true if there are no [Catastrophe]s in the database, false otherwise.
     */
    fun isCatastropheEmpty(): Boolean

    /**
     * Rewrites the [Catastrophe] table with the given [catastrophes].
     */
    fun rewriteCatastrophes(catastrophes: List<Catastrophe>)

    /**
     * Get [Catastrophe]s from the database.
     */
    fun getCatastrophes(): List<Catastrophe>
}
