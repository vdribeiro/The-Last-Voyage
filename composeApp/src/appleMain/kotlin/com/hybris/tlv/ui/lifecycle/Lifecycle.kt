@file:ShadowedInTesting

package com.hybris.tlv.ui.lifecycle

import platform.Foundation.NSNotificationCenter
import platform.Foundation.NSOperationQueue
import platform.UIKit.UIApplicationDidBecomeActiveNotification
import platform.UIKit.UIApplicationWillResignActiveNotification
import platform.darwin.NSObjectProtocol
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import com.hybris.tlv.test.ShadowedInTesting

@Composable
internal actual fun Register(
    key: Any,
    onBackground: () -> Unit,
    onForeground: () -> Unit,
) {
    val lifecycleOwner = NSNotificationCenter.defaultCenter
    DisposableEffect(key1 = key) {
        val pauseObserver = lifecycleOwner.observe(
            name = UIApplicationWillResignActiveNotification,
            onObserve = onBackground
        )
        val resumeObserver = lifecycleOwner.observe(
            name = UIApplicationDidBecomeActiveNotification,
            onObserve = onForeground
        )

        onDispose {
            lifecycleOwner.removeObserver(observer = pauseObserver)
            lifecycleOwner.removeObserver(observer = resumeObserver)
        }
    }
}

internal fun NSNotificationCenter.observe(
    name: String?,
    key: Any? = null,
    onObserve: () -> Unit
): NSObjectProtocol = addObserverForName(
    name = name,
    `object` = key,
    queue = NSOperationQueue.mainQueue
) { _ -> onObserve() }
