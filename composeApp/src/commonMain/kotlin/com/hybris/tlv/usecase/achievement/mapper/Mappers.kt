package com.hybris.tlv.usecase.achievement.mapper

import com.hybris.tlv.database.AchievementSchema
import com.hybris.tlv.http.client.json
import com.hybris.tlv.http.getBoolean
import com.hybris.tlv.http.getString
import com.hybris.tlv.usecase.achievement.model.Achievement
import com.hybris.tlv.usecase.achievement.model.Precondition
import com.hybris.tlv.usecase.achievement.remote.AchievementApi.Companion.ACHIEVEMENTS_DESCRIPTION
import com.hybris.tlv.usecase.achievement.remote.AchievementApi.Companion.ACHIEVEMENTS_ID
import com.hybris.tlv.usecase.achievement.remote.AchievementApi.Companion.ACHIEVEMENTS_NAME
import com.hybris.tlv.usecase.achievement.remote.AchievementApi.Companion.ACHIEVEMENTS_PRECONDITIONS
import com.hybris.tlv.usecase.achievement.remote.AchievementApi.Companion.ACHIEVEMENTS_STATUS

internal fun Achievement.toAchievementMap(): Map<String, Any> =
    buildMap {
        put(key = ACHIEVEMENTS_ID, value = id)
        put(key = ACHIEVEMENTS_NAME, value = name)
        put(key = ACHIEVEMENTS_DESCRIPTION, value = description)
        put(key = ACHIEVEMENTS_PRECONDITIONS, value = json.encodeToString(value = preconditions))
        put(key = ACHIEVEMENTS_STATUS, value = status)
    }

internal fun Map<String, Any>.toAchievement(): Achievement =
    Achievement(
        id = getString(key = ACHIEVEMENTS_ID)!!,
        name = getString(key = ACHIEVEMENTS_NAME)!!,
        description = getString(key = ACHIEVEMENTS_DESCRIPTION)!!,
        preconditions = json.decodeFromString<Precondition>(string = getString(key = ACHIEVEMENTS_PRECONDITIONS)!!),
        status = getBoolean(key = ACHIEVEMENTS_STATUS)!!
    )

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