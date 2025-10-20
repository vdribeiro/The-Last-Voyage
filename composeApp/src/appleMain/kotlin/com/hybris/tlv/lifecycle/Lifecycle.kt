package com.hybris.tlv.lifecycle

import platform.Foundation.NSNotificationCenter
import platform.Foundation.NSOperationQueue
import platform.UIKit.UIApplicationDidBecomeActiveNotification
import platform.UIKit.UIApplicationWillResignActiveNotification
import androidx.compose.runtime.Composable

@Composable
internal actual fun Register(
    key: Any,
    onBackground: () -> Unit,
    onForeground: () -> Unit,
) {
    val lifecycleOwner = NSNotificationCenter.defaultCenter
    DisposableLifecycleCoroutine(key) {
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
) = addObserverForName(
    name = name,
    `object` = key,
    queue = NSOperationQueue.mainQueue
) { _ -> onObserve() }
