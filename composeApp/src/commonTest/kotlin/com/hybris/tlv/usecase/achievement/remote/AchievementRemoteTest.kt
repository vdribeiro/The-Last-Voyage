package com.hybris.tlv.usecase.achievement.remote

import com.hybris.tlv.mock.Mock
import com.hybris.tlv.mock.achievements
import com.hybris.tlv.usecase.Result
import com.hybris.tlv.usecase.SyncResult
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.runBlocking

internal class AchievementRemoteTest {

    private val mock = Mock()

    @Test
    fun `write and get achievements`() = runBlocking {
        assertEquals(expected = Result.Success(list = emptyList()), actual = mock.achievementApi.getAchievements().last())
        assertEquals(expected = SyncResult.Success, actual = mock.achievementApi.rewriteAchievements(achievements = achievements).last())
        assertEquals(expected = Result.Success(list = achievements), actual = mock.achievementApi.getAchievements().last())
    }
}
