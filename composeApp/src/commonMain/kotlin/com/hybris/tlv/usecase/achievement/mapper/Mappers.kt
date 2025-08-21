package com.hybris.tlv.usecase.achievement.mapper

import com.hybris.tlv.database.AchievementSchema
import com.hybris.tlv.serializer.json
import com.hybris.tlv.usecase.achievement.model.Achievement
import com.hybris.tlv.usecase.achievement.model.Precondition

internal fun Achievement.toAchievementSchema(): AchievementSchema =
    AchievementSchema(
        id = id,
        name = name,
        description = description,
        preconditions = json.encodeToString(value = preconditions),
        status = status
    )

internal fun AchievementSchema.toAchievement(): Achievement =
    Achievement(
        id = id,
        name = name,
        description = description,
        preconditions = json.decodeFromString<Precondition>(string = preconditions),
        status = status
    )