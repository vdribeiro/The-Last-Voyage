package com.hybris.tlv.usecase.translation.remote

import com.hybris.tlv.usecase.Result
import com.hybris.tlv.usecase.translation.model.domain.Translation

internal interface TranslationRemote {

    /**
     * Get translations from the API.
     */
    suspend fun getTranslations(): Result<Translation>
}
