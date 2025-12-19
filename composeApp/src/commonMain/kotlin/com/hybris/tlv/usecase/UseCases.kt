package com.hybris.tlv.usecase

import com.hybris.tlv.usecase.achievement.AchievementUseCases
import com.hybris.tlv.usecase.catastrophe.CatastropheUseCases
import com.hybris.tlv.usecase.credit.CreditUseCases
import com.hybris.tlv.usecase.event.EventUseCases
import com.hybris.tlv.usecase.gamesession.GameSessionUseCases
import com.hybris.tlv.usecase.ship.ShipUseCases
import com.hybris.tlv.usecase.space.ArchiveUseCases
import com.hybris.tlv.usecase.space.SpaceUseCases
import com.hybris.tlv.usecase.sync.SyncUseCases
import com.hybris.tlv.usecase.translation.TranslationUseCases

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
