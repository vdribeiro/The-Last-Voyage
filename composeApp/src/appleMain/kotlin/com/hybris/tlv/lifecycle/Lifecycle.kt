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
    onDestroy: () -> Unit
) {
    DisposableEffect(keys = arrayOf(key)) {
        val pauseObserver = NSNotificationCenter.defaultCenter.addObserverForName(
            name = UIApplicationWillResignActiveNotification,
            `object` = null,
            queue = NSOperationQueue.mainQueue
        ) { _ ->
            onPause()
        }

        val resumeObserver = NSNotificationCenter.defaultCenter.addObserverForName(
            name = UIApplicationDidBecomeActiveNotification,
            `object` = null,
            queue = NSOperationQueue.mainQueue
        ) { _ ->
            onResume()
        }

        onDispose {
            NSNotificationCenter.defaultCenter.removeObserver(pauseObserver)
            NSNotificationCenter.defaultCenter.removeObserver(resumeObserver)
            onDestroy()
        }
    }
}
