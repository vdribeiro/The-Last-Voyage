package com.hybris.tlv.usecase

import com.hybris.tlv.config.ConfigManager
import com.hybris.tlv.usecase.achievement.AchievementGateway
import com.hybris.tlv.usecase.achievement.AchievementInternalUseCases
import com.hybris.tlv.usecase.achievement.AchievementUseCases
import com.hybris.tlv.usecase.achievement.local.AchievementLocal
import com.hybris.tlv.usecase.credit.CreditGateway
import com.hybris.tlv.usecase.credit.CreditInternalUseCases
import com.hybris.tlv.usecase.credit.CreditUseCases
import com.hybris.tlv.usecase.credit.local.CreditLocal
import com.hybris.tlv.usecase.earth.EarthGateway
import com.hybris.tlv.usecase.earth.EarthInternalUseCases
import com.hybris.tlv.usecase.earth.EarthUseCases
import com.hybris.tlv.usecase.earth.local.EarthLocal
import com.hybris.tlv.usecase.event.EventGateway
import com.hybris.tlv.usecase.event.EventInternalUseCases
import com.hybris.tlv.usecase.event.EventUseCases
import com.hybris.tlv.usecase.event.local.EventLocal
import com.hybris.tlv.usecase.gamesession.GameSessionGateway
import com.hybris.tlv.usecase.gamesession.GameSessionUseCases
import com.hybris.tlv.usecase.gamesession.local.GameSessionLocal
import com.hybris.tlv.usecase.ship.ShipGateway
import com.hybris.tlv.usecase.ship.ShipInternalUseCases
import com.hybris.tlv.usecase.ship.ShipUseCases
import com.hybris.tlv.usecase.ship.local.ShipLocal
import com.hybris.tlv.usecase.space.SpaceGateway
import com.hybris.tlv.usecase.space.SpaceInternalUseCases
import com.hybris.tlv.usecase.space.SpaceUseCases
import com.hybris.tlv.usecase.space.local.SpaceLocal
import com.hybris.tlv.usecase.sync.SyncGateway
import com.hybris.tlv.usecase.sync.SyncUseCases
import com.hybris.tlv.usecase.translation.TranslationInternalUseCases

internal class Gateways(
    config: ConfigManager,
    earthDao: EarthLocal,
    shipDao: ShipLocal,
    spaceDao: SpaceLocal,
    eventDao: EventLocal,
    gameSessionDao: GameSessionLocal,
    achievementDao: AchievementLocal,
    creditDao: CreditLocal,
    internalTranslation: TranslationInternalUseCases,
    internalEarth: EarthInternalUseCases,
    internalShip: ShipInternalUseCases,
    internalSpace: SpaceInternalUseCases,
    internalEvent: EventInternalUseCases,
    internalAchievement: AchievementInternalUseCases,
    internalCredit: CreditInternalUseCases
): UseCases {

    override val earth: EarthUseCases = EarthGateway(earthDao = earthDao)
    override val ship: ShipUseCases = ShipGateway(shipDao = shipDao)
    override val space: SpaceUseCases = SpaceGateway(spaceDao = spaceDao)
    override val event: EventUseCases = EventGateway(eventDao = eventDao)
    override val gameSession: GameSessionUseCases = GameSessionGateway(
        gameSessionDao = gameSessionDao,
        shipInternalUseCases = internalShip,
        spaceInternalUseCases = internalSpace
    )
    override val achievement: AchievementUseCases = AchievementGateway(achievementDao = achievementDao)
    override val credit: CreditUseCases = CreditGateway(creditDao = creditDao)
    override val sync: SyncUseCases = SyncGateway(
        storage = config,
        internalTranslation = internalTranslation,
        internalEarth = internalEarth,
        internalShip = internalShip,
        internalSpace = internalSpace,
        internalEvent = internalEvent,
        internalAchievement = internalAchievement,
        internalCredit = internalCredit
    )
}
