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
import com.hybris.tlv.usecase.space.SpaceGateway
import com.hybris.tlv.usecase.space.SpaceUseCases
import com.hybris.tlv.usecase.sync.SyncGateway
import com.hybris.tlv.usecase.sync.SyncUseCases
import database.AppDatabase
import io.ktor.client.HttpClient

internal class Gateways(
    dispatcher: Dispatcher,
    config: ConfigManager,
    httpClient: HttpClient,
    database: AppDatabase,
): UseCases {
    override val learning: LearningUseCases = LearningGateway(database = database)
    override val catastrophe: CatastropheUseCases = CatastropheGateway(database = database)
    override val ship: ShipUseCases = ShipGateway(database = database)
    override val space: SpaceUseCases = SpaceGateway(database = database)
    override val event: EventUseCases = EventGateway(database = database)
    override val gameSession: GameSessionUseCases = GameSessionGateway(database = database)
    override val achievement: AchievementUseCases = AchievementGateway(database = database)
    override val credit: CreditUseCases = CreditGateway(database = database)
    override val sync: SyncUseCases = SyncGateway(
        dispatcher = dispatcher,
        config = config,
        httpClient = httpClient,
        database = database
    )
}
