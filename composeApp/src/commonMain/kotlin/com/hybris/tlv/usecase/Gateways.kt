package com.hybris.tlv.usecase

import io.ktor.client.HttpClient
import com.hybris.tlv.config.ConfigManager
import com.hybris.tlv.usecase.achievement.AchievementGateway
import com.hybris.tlv.usecase.achievement.AchievementUseCases
import com.hybris.tlv.usecase.catastrophe.CatastropheGateway
import com.hybris.tlv.usecase.catastrophe.CatastropheUseCases
import com.hybris.tlv.usecase.credit.CreditGateway
import com.hybris.tlv.usecase.credit.CreditUseCases
import com.hybris.tlv.usecase.event.EventGateway
import com.hybris.tlv.usecase.event.EventUseCases
import com.hybris.tlv.usecase.gamesession.GameSessionGateway
import com.hybris.tlv.usecase.gamesession.GameSessionUseCases
import com.hybris.tlv.usecase.ship.ShipGateway
import com.hybris.tlv.usecase.ship.ShipUseCases
import com.hybris.tlv.usecase.space.ArchiveGateway
import com.hybris.tlv.usecase.space.ArchiveUseCases
import com.hybris.tlv.usecase.space.SpaceGateway
import com.hybris.tlv.usecase.space.SpaceUseCases
import com.hybris.tlv.usecase.sync.SyncGateway
import com.hybris.tlv.usecase.sync.SyncUseCases
import com.hybris.tlv.usecase.translation.TranslationGateway
import com.hybris.tlv.usecase.translation.TranslationUseCases
import database.AppDatabase

internal class Gateways(
    config: ConfigManager,
    database: AppDatabase,
    httpClient: HttpClient
): UseCases {
    override val translation: TranslationUseCases = TranslationGateway(
        httpClient = httpClient,
        database = database
    )
    override val archive: ArchiveUseCases = ArchiveGateway(
        httpClient = httpClient,
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
