package com.hybris.tlv.usecase

import com.hybris.tlv.config.ConfigManager
import com.hybris.tlv.flow.Dispatcher
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
import com.hybris.tlv.usecase.learning.LearningGateway
import com.hybris.tlv.usecase.learning.LearningUseCases
import com.hybris.tlv.usecase.ship.ShipGateway
import com.hybris.tlv.usecase.ship.ShipUseCases
import com.hybris.tlv.usecase.space.ArchiveGateway
import com.hybris.tlv.usecase.space.ArchiveUseCases
import com.hybris.tlv.usecase.space.SpaceGateway
import com.hybris.tlv.usecase.space.SpaceUseCases
import com.hybris.tlv.usecase.translation.TranslationGateway
import com.hybris.tlv.usecase.translation.TranslationUseCases
import database.AppDatabase
import io.ktor.client.HttpClient

internal class Gateways(
    dispatcher: Dispatcher,
    config: ConfigManager,
    httpClient: HttpClient,
    database: AppDatabase,
): UseCases {
    override val translation: TranslationUseCases = TranslationGateway(
        dispatcher = dispatcher,
        config = config,
        httpClient = httpClient,
        database = database
    )
    override val archive: ArchiveUseCases = ArchiveGateway(
        httpClient = httpClient,
    )
    override val learning: LearningUseCases = LearningGateway(
        config = config,
        httpClient = httpClient,
        database = database
    )
    override val catastrophe: CatastropheUseCases = CatastropheGateway(
        config = config,
        httpClient = httpClient,
        database = database
    )
    override val ship: ShipUseCases = ShipGateway(
        config = config,
        httpClient = httpClient,
        database = database
    )
    override val space: SpaceUseCases = SpaceGateway(
        config = config,
        httpClient = httpClient,
        database = database
    )
    override val event: EventUseCases = EventGateway(
        config = config,
        httpClient = httpClient,
        database = database
    )
    override val gameSession: GameSessionUseCases = GameSessionGateway(database = database)
    override val achievement: AchievementUseCases = AchievementGateway(
        config = config,
        httpClient = httpClient,
        database = database
    )
    override val credit: CreditUseCases = CreditGateway(
        config = config,
        httpClient = httpClient,
        database = database
    )
}
