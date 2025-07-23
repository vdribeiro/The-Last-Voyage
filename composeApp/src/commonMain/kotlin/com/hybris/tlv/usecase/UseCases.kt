package com.hybris.tlv.usecase

import com.hybris.tlv.usecase.achievement.AchievementUseCases
import com.hybris.tlv.usecase.credits.CreditsUseCases
import com.hybris.tlv.usecase.earth.EarthUseCases
import com.hybris.tlv.usecase.event.EventUseCases
import com.hybris.tlv.usecase.exoplanet.ExoplanetUseCases
import com.hybris.tlv.usecase.gamesession.GameSessionUseCases
import com.hybris.tlv.usecase.ship.ShipUseCases
import com.hybris.tlv.usecase.space.SpaceUseCases
import com.hybris.tlv.usecase.translation.TranslationUseCases

internal interface UseCases {

    val translation: TranslationUseCases
    val earth: EarthUseCases
    val ship: ShipUseCases
    val space: SpaceUseCases
    val event: EventUseCases
    val exoplanet: ExoplanetUseCases
    val gameSession: GameSessionUseCases
    val achievement: AchievementUseCases
    val credits: CreditsUseCases
}
