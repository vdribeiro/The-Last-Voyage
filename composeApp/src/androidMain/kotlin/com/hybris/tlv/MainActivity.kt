package com.hybris.tlv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.hybris.tlv.Application.dependency
import com.hybris.tlv.test.ExcludeFromTesting
import com.hybris.tlv.ui.App
import com.hybris.tlv.ui.cheats.enableGestureCheats

@ExcludeFromTesting
class MainActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val dependency by dependency.collectAsState()
            App(
                modifier = Modifier.enableGestureCheats(navController = navController),
                navController = navController,
                dependency = dependency
            )
        }
    }
}
