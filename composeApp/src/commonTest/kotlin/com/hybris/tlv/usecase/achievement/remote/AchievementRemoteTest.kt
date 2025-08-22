package com.hybris.tlv.usecase.achievement.remote

import com.hybris.tlv.http.Result
import com.hybris.tlv.mock.Mock
import com.hybris.tlv.mock.achievements
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.runBlocking

internal class AchievementRemoteTest {

    private val mock = Mock()

    @Test
    fun `write and get achievements`() = runBlocking {
        assertEquals(expected = Result.Success(list = achievements), actual = mock.achievementApi.getAchievements())
    }
}
