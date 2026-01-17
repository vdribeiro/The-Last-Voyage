package com.hybris.tlv.domain.usecase

import com.hybris.tlv.domain.usecase.achievement.AchievementUseCases
import com.hybris.tlv.domain.usecase.catastrophe.CatastropheUseCases
import com.hybris.tlv.domain.usecase.credit.CreditUseCases
import com.hybris.tlv.domain.usecase.event.EventUseCases
import com.hybris.tlv.domain.usecase.gamesession.GameSessionUseCases
import com.hybris.tlv.domain.usecase.ship.ShipUseCases
import com.hybris.tlv.domain.usecase.space.ArchiveUseCases
import com.hybris.tlv.domain.usecase.space.SpaceUseCases
import com.hybris.tlv.domain.usecase.sync.SyncUseCases
import com.hybris.tlv.domain.usecase.translation.TranslationUseCases

/**
 * A central interface that groups together all the use cases of the application.
 */
internal interface UseCases {
    val archive: ArchiveUseCases
    val translation: TranslationUseCases
    val catastrophe: CatastropheUseCases
    val ship: ShipUseCases
    val space: SpaceUseCases
    val event: EventUseCases
    val gameSession: GameSessionUseCases
    val achievement: AchievementUseCases
    val credit: CreditUseCases
    val sync: SyncUseCases
}
