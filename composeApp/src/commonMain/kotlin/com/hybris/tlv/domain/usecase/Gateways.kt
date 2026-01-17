package com.hybris.tlv.domain.usecase

import io.ktor.client.HttpClient
import com.hybris.tlv.data.config.ConfigManager
import com.hybris.tlv.domain.usecase.achievement.AchievementGateway
import com.hybris.tlv.domain.usecase.achievement.AchievementUseCases
import com.hybris.tlv.domain.usecase.catastrophe.CatastropheGateway
import com.hybris.tlv.domain.usecase.catastrophe.CatastropheUseCases
import com.hybris.tlv.domain.usecase.credit.CreditGateway
import com.hybris.tlv.domain.usecase.credit.CreditUseCases
import com.hybris.tlv.domain.usecase.event.EventGateway
import com.hybris.tlv.domain.usecase.event.EventUseCases
import com.hybris.tlv.domain.usecase.gamesession.GameSessionGateway
import com.hybris.tlv.domain.usecase.gamesession.GameSessionUseCases
import com.hybris.tlv.domain.usecase.ship.ShipGateway
import com.hybris.tlv.domain.usecase.ship.ShipUseCases
import com.hybris.tlv.domain.usecase.space.ArchiveGateway
import com.hybris.tlv.domain.usecase.space.ArchiveUseCases
import com.hybris.tlv.domain.usecase.space.SpaceGateway
import com.hybris.tlv.domain.usecase.space.SpaceUseCases
import com.hybris.tlv.domain.usecase.sync.SyncGateway
import com.hybris.tlv.domain.usecase.sync.SyncUseCases
import com.hybris.tlv.domain.usecase.translation.TranslationGateway
import com.hybris.tlv.domain.usecase.translation.TranslationUseCases
import database.AppDatabase

internal class Gateways(
    config: ConfigManager,
    database: AppDatabase,
    httpClient: HttpClient
): UseCases {
    override val archive: ArchiveUseCases = ArchiveGateway(
        httpClient = httpClient,
    )
    override val translation: TranslationUseCases = TranslationGateway(
        httpClient = httpClient,
        database = database
    )
    override val catastrophe: CatastropheUseCases = CatastropheGateway(
        httpClient = httpClient,
        database = database
    )
    override val ship: ShipUseCases = ShipGateway(
        httpClient = httpClient,
        database = database
    )
    override val space: SpaceUseCases = SpaceGateway(
        httpClient = httpClient,
        database = database
    )
    override val event: EventUseCases = EventGateway(
        httpClient = httpClient,
        database = database
    )
    override val gameSession: GameSessionUseCases = GameSessionGateway(
        database = database
    )
    override val achievement: AchievementUseCases = AchievementGateway(
        httpClient = httpClient,
        database = database
    )
    override val credit: CreditUseCases = CreditGateway(
        httpClient = httpClient,
        database = database
    )
    override val sync: SyncUseCases = SyncGateway(
        config = config,
        database = database,
        archiveUseCases = archive,
        translationUseCases = translation,
        catastropheUseCases = catastrophe,
        shipUseCases = ship,
        spaceUseCases = space,
        eventUseCases = event,
        achievementUseCases = achievement,
        creditUseCases = credit
    )
}
