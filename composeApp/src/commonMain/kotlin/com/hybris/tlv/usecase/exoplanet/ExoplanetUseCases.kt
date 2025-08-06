package com.hybris.tlv.usecase.exoplanet

import com.hybris.tlv.usecase.exoplanet.model.Params
import com.hybris.tlv.usecase.exoplanet.model.Score

internal interface ExoplanetUseCases {

    /**
     * Calculates the habitability score for a given planet in a stellarHost.
     */
    fun calculateHabitability(params: Params): Score
}
