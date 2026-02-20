package com.hybris.tlv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.hybris.tlv.TLV.App
import com.hybris.tlv.test.ExcludeFromTesting
import com.hybris.tlv.ui.cheats.enableGestureCheats

@ExcludeFromTesting
class MainActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            App(
                modifier = Modifier.enableGestureCheats(navController = navController),
                navController = navController
            )
        }
    }
}
