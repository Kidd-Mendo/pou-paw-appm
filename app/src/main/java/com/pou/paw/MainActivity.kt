package com.pou.paw

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.pou.paw.ui.AppNavigation
import com.pou.paw.ui.theme.PouPawTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PouPawTheme {
                AppNavigation()
            }
        }
    }
}
