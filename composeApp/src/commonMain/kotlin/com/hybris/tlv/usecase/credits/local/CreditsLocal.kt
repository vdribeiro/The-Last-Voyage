package com.hybris.tlv.usecase.credits.local

import com.hybris.tlv.usecase.credits.model.Credits

internal interface CreditsLocal {

    /**
     * Returns true if there are no [Credits] in the database, false otherwise.
     */
    fun isCreditsEmpty(): Boolean

    /**
     * Rewrites the [Credits] table with the given [credits].
     */
    fun rewriteCredits(credits: List<Credits>)

    /**
     * Get [Credits] from the database.
     */
    fun getCredits(): List<Credits>
}
