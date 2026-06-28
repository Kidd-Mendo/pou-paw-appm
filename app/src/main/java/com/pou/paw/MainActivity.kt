package com.pou.paw

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.*
import androidx.core.os.LocaleListCompat
import com.pou.paw.ui.AppNavigation
import com.pou.paw.ui.theme.PouPawTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val prefs = getSharedPreferences("root_paw_settings", Context.MODE_PRIVATE)
        
        // Al arrancar, forzamos el idioma que esté guardado (por defecto Español "es")
        val langCode = prefs.getString("language", "es") ?: "es"
        
        val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(langCode)
        AppCompatDelegate.setApplicationLocales(appLocale)

        setContent {
            var themePref by remember { 
                mutableStateOf(prefs.getString("theme", "Claro") ?: "Claro") 
            }

            // Escuchar cambios en SharedPreferences en tiempo real
            val listener = remember {
                android.content.SharedPreferences.OnSharedPreferenceChangeListener { p, key ->
                    if (key == "theme") {
                        themePref = p.getString("theme", "Claro") ?: "Claro"
                    } else if (key == "language") {
                        val newCode = p.getString("language", "es") ?: "es"
                        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(newCode))
                    }
                }
            }

            DisposableEffect(prefs) {
                prefs.registerOnSharedPreferenceChangeListener(listener)
                onDispose { prefs.unregisterOnSharedPreferenceChangeListener(listener) }
            }

            val isDarkTheme = when (themePref) {
                "Oscuro" -> true
                "Claro" -> false
                else -> androidx.compose.foundation.isSystemInDarkTheme()
            }

            PouPawTheme(darkTheme = isDarkTheme) {
                AppNavigation()
            }
        }
    }
}
