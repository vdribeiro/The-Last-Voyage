package com.hybris.tlv.data.resource

import org.jetbrains.compose.resources.Font
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import com.hybris.tlv.test.ExcludeFromTesting
import thelastvoyage.composeapp.generated.resources.Inter_18pt_Regular
import thelastvoyage.composeapp.generated.resources.Res

/**
 * Resource index for fonts in [commonMain/composeResources/font].
 */
// TODO - typography
@ExcludeFromTesting
internal sealed class FontResource {
    abstract val family: FontFamily @Composable get

    data object Inter: FontResource() {
        override val family: FontFamily
            @Composable get() = FontFamily(
                Font(resource = Res.font.Inter_18pt_Regular)
            )
    }
}