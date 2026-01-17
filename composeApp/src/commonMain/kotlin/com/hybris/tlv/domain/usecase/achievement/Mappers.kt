package com.hybris.tlv.domain.usecase.achievement

import com.hybris.tlv.data.database.AchievementSchema
import com.hybris.tlv.data.serializer.decode
import com.hybris.tlv.data.serializer.encode
import com.hybris.tlv.domain.usecase.achievement.model.Achievement
import com.hybris.tlv.domain.usecase.achievement.model.Precondition

internal fun Achievement.toAchievementSchema(): AchievementSchema =
    AchievementSchema(
        id = id,
        description = description,
        preconditions = encode(value = preconditions).orEmpty(),
        done = done
    )

internal fun AchievementSchema.toAchievement(): Achievement =
    Achievement(
        id = id,
        description = description,
        preconditions = decode<Precondition>(value = preconditions) ?: Precondition(),
        done = done
    )
