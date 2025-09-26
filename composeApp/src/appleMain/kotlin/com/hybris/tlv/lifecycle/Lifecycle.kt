package com.hybris.tlv.lifecycle

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import platform.Foundation.NSNotificationCenter
import platform.Foundation.NSOperationQueue
import platform.UIKit.UIApplicationDidBecomeActiveNotification
import platform.UIKit.UIApplicationWillResignActiveNotification

@Composable
internal actual fun Register(
    key: Any,
    onPause: () -> Unit,
    onResume: () -> Unit,
) {
    val lifecycleOwner = NSNotificationCenter.defaultCenter
    DisposableEffect(keys = arrayOf(key)) {
        val pauseObserver = lifecycleOwner.observe(
            name = UIApplicationWillResignActiveNotification,
            onObserve = onPause
        )
        val resumeObserver = lifecycleOwner.observe(
            name = UIApplicationDidBecomeActiveNotification,
            onObserve = onResume
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
