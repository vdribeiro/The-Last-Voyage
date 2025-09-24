package com.hybris.tlv.ui.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.SemanticsPropertyReceiver
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

/**
 * Applies modifiers in the optimal order.
 * - [tag]: The test tag to allow the element to be found in tests.
 * - [minWidth]: The minimum width of the element.
 * - [maxWidth]: The maximum width of the element. [Dp.Infinity] will fill the available width.
 * - [minHeight]: The minimum height of the element.
 * - [maxHeight]: The maximum height of the element. [Dp.Infinity] will fill the available height.
 * - [verticalScroll]: The scroll state for the vertical scroll if the element is to be scrollable vertically.
 * - [horizontalScroll]: The scroll state for the horizontal scroll if the element is to be scrollable horizontal.
 * - [enabled]: The enabled state of the click interaction. When false, onClick, and this modifier will appear disabled for accessibility services.
 * - [rippleEffect]: Whether the element has a ripple effect on the click interaction.
 * - [debounceTime]: The debounce time for the click interaction to prevent multiple fast clicks.
 * - [onClick]: The click listener for the element.
 * - [shape]: The shape of the element.
 * - [elevation]: The elevation for the shadow.
 * - [backgroundColor]: The color used to draws the shape behind the content.
 * - [border]: The border clipped by the shape.
 * - [padding]: The padding of the element.
 * - [mergeDescendants]: Whether the semantic information provided by the owning component and its descendants should be treated as one logical entity.
 * - [properties]: Properties to add to the semantics.
 */
@Composable
fun Modifier.thenIf(
    tag: String? = null,
    minWidth: Dp = Dp.Unspecified,
    maxWidth: Dp = Dp.Unspecified,
    minHeight: Dp = Dp.Unspecified,
    maxHeight: Dp = Dp.Unspecified,
    verticalScroll: ScrollState? = null,
    horizontalScroll: ScrollState? = null,
    enabled: Boolean = true,
    rippleEffect: Boolean = true,
    debounceTime: Duration = 500.milliseconds,
    onClick: (() -> Unit)? = null,
    shape: Shape = RectangleShape,
    elevation: Dp = Dp.Unspecified,
    backgroundColor: Color = Color.Unspecified,
    border: BorderStroke? = null,
    padding: PaddingValues? = null,
    mergeDescendants: Boolean = false,
    properties: (SemanticsPropertyReceiver.() -> Unit)? = null,
): Modifier = this
    .then(other = if (tag != null) Modifier.testTag(tag = tag) else Modifier)
    // Establish the canvas dimensions
    .then(
        other = if (minWidth != Dp.Unspecified || maxWidth != Dp.Unspecified || minHeight != Dp.Unspecified || maxHeight != Dp.Unspecified) Modifier.sizeIn(
            minWidth = minWidth,
            minHeight = minHeight,
            maxWidth = maxWidth,
            maxHeight = maxHeight
        ) else Modifier
    )
    // Apply scroll modifiers
    .then(other = if (verticalScroll != null) Modifier.verticalScroll(state = verticalScroll) else Modifier)
    .then(other = if (horizontalScroll != null) Modifier.horizontalScroll(state = horizontalScroll) else Modifier)
    // The clickable modifier must be applied before the padding for the entire area to be clickable, otherwise, only the inner area is interactive
    .then(
        other = if (onClick != null) Modifier.debouncedClickable(
            enabled = enabled,
            rippleEffect = rippleEffect,
            debounceTime = debounceTime,
            onClick = onClick
        ) else Modifier
    )
    // Apply shadow before background and border as it is drawn based on the component's shape but exists underneath it and outside its layout bounds
    .then(
        other = if (elevation != Dp.Unspecified) Modifier.shadow(
            elevation = elevation,
            shape = shape,
            clip = false
        ) else Modifier
    )
    // Paint the canvas
    .then(
        other = if (backgroundColor != Color.Unspecified) Modifier.background(
            color = backgroundColor,
            shape = shape
        ) else Modifier
    )
    // Apply inner filling
    .then(
        other = if (border != null) Modifier.border(
            border = border,
            shape = shape
        ) else Modifier
    )
    // Inset the content area after all sizing and drawing is complete
    .then(other = if (padding != null) Modifier.padding(paddingValues = padding) else Modifier)
    // Semantics is applied last to override the previous modifier semantics
    .then(
        other = if (properties != null) Modifier.semantics(
            mergeDescendants = mergeDescendants,
            properties = properties
        ) else Modifier
    )

/**
 * Make the element clickable within its bounds and prevent multiple fast clicks.
 */
@OptIn(ExperimentalTime::class)
internal fun Modifier.debouncedClickable(
    enabled: Boolean = true,
    rippleEffect: Boolean = true,
    debounceTime: Duration = 500.milliseconds,
    onClick: () -> Unit
): Modifier = composed {
    var lastClickTime by remember { mutableStateOf(value = Instant.DISTANT_PAST) }
    val onClick = {
        val now = Clock.System.now()
        val elapsed = now - lastClickTime
        if (elapsed > debounceTime) {
            lastClickTime = now
            onClick()
        }
    }
    if (rippleEffect) clickable(enabled = enabled, onClick = onClick) else clickable(
        enabled = enabled,
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        onClick = onClick
    )
}
