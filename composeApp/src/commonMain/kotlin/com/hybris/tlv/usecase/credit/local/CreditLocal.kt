package com.hybris.tlv.usecase.credit.local

import com.hybris.tlv.usecase.credit.model.Credit

internal interface CreditLocal {

    /**
     * Returns true if there are no [Credit]s in the database, false otherwise.
     */
    fun isCreditEmpty(): Boolean

    /**
     * Rewrites the [Credit] table with the given [credits].
     */
    fun rewriteCredits(credits: List<Credit>)

    /**
     * Get [Credit] from the database.
     */
    fun getCredits(): List<Credit>
}
