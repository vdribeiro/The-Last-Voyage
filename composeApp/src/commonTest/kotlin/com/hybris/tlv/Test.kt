package com.hybris.tlv

import kotlinx.coroutines.runBlocking
import com.hybris.tlv.audio.AudioPlayer
import com.hybris.tlv.database.createSqlDriver
import com.hybris.tlv.dependency.Dependency
import com.hybris.tlv.http.TestEngine
import com.hybris.tlv.usecase.UseCases

internal val dependency: Dependency by lazy {
    Dependency(
        sqlDriver = createSqlDriver(inMemory = true),
        httpEngine = TestEngine.mock,
        audioPlayer = AudioPlayer(),
    )
}

internal fun reset() {
    runBlocking {
        dependency.useCases.sync.reset()
    }
}

internal val useCases: UseCases by lazy { dependency.useCases }
