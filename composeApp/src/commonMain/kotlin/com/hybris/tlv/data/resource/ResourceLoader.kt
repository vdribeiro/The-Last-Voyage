package com.hybris.tlv.data.resource

import kotlinx.coroutines.withContext
import com.hybris.tlv.core.flow.Dispatcher
import com.hybris.tlv.core.telemetry.Telemetry
import com.hybris.tlv.data.serializer.loadFromJsonResource
import com.hybris.tlv.domain.usecase.translation.model.Translation
import thelastvoyage.composeapp.generated.resources.Res

/**
 * Reads the content of the resource file at the specified path and returns it as a string.
 * Returns null on failure.
 */
internal suspend fun loadResource(path: String): String? = withContext(context = Dispatcher.IO) {
    runCatching {
        Res.readBytes(path = path).decodeToString()
    }.onFailure {
        Telemetry.error(tag = TAG, message = "Unable to read resource", throwable = it)
    }.getOrNull()
}

internal suspend fun loadAllTranslationsFromJsonResource(): List<Translation> =
    loadFromJsonResource<Translation>(json = JsonResource.Translations) +
            loadFromJsonResource<Translation>(json = JsonResource.CatastrophesTranslations) +
            loadFromJsonResource<Translation>(json = JsonResource.EnginesTranslations) +
            loadFromJsonResource<Translation>(json = JsonResource.EventsTranslations) +
            loadFromJsonResource<Translation>(json = JsonResource.AchievementsTranslations)

private const val TAG = "ResourceLoader"
