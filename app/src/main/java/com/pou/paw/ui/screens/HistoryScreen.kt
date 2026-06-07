package com.pou.paw.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pou.paw.data.model.UserStats

@Composable
fun HistoryScreen(stats: UserStats) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Historial y Estadísticas", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Racha Actual: ${stats.streakDays} días", style = MaterialTheme.typography.titleLarge)
                Text("Tareas Totales: ${stats.totalTasksCompleted}", style = MaterialTheme.typography.bodyLarge)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text("Logros", style = MaterialTheme.typography.titleMedium)
        LazyColumn {
            items(stats.achievements) { achievement ->
                ListItem(
                    headlineContent = { Text(achievement) },
                    leadingContent = { Icon(painterResource(id = android.R.drawable.btn_star), contentDescription = null) }
                )
            }
        }
    }
}

@Composable
fun painterResource(id: Int) = androidx.compose.ui.res.painterResource(id)
