package com.pou.paw.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.pou.paw.R
import com.pou.paw.ui.viewmodel.SettingsUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    uiState: SettingsUiState,
    onBackClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onToggleProfileDialog: (Boolean) -> Unit,
    onToggleThemeDialog: (Boolean) -> Unit,
    onToggleLanguageDialog: (Boolean) -> Unit,
    onToggleAboutDialog: (Boolean) -> Unit,
    onTogglePasswordDialog: (Boolean) -> Unit,
    onToggleNotifications: (Boolean) -> Unit,
    onToggleReminders: (Boolean) -> Unit,
    onToggleSounds: (Boolean) -> Unit,
    onSetTheme: (String) -> Unit,
    onSetLanguage: (String) -> Unit,
    onUpdateProfile: (String, String) -> Unit,
    onChangePassword: (String) -> Unit
) {
    val context = LocalContext.current
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.settings_title)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            SettingsSection(title = stringResource(R.string.section_account)) {
                SettingsItem(
                    icon = Icons.Default.Person,
                    title = stringResource(R.string.edit_profile),
                    onClick = { onToggleProfileDialog(true) }
                )
                SettingsItem(
                    icon = Icons.Default.Lock,
                    title = stringResource(R.string.change_password),
                    onClick = { onTogglePasswordDialog(true) }
                )
            }

            SettingsSection(title = stringResource(R.string.section_preferences)) {
                SettingsItem(
                    icon = Icons.Default.Notifications,
                    title = stringResource(R.string.notifications),
                    trailing = {
                        Switch(
                            checked = uiState.notificationsEnabled,
                            onCheckedChange = onToggleNotifications
                        )
                    }
                )
                SettingsItem(
                    icon = Icons.Default.Schedule,
                    title = stringResource(R.string.reminders),
                    trailing = {
                        Switch(
                            checked = uiState.remindersEnabled,
                            onCheckedChange = onToggleReminders
                        )
                    }
                )
                SettingsItem(
                    icon = Icons.AutoMirrored.Filled.VolumeUp,
                    title = stringResource(R.string.sounds),
                    trailing = {
                        Switch(
                            checked = uiState.soundsEnabled,
                            onCheckedChange = onToggleSounds
                        )
                    }
                )
            }

            SettingsSection(title = stringResource(R.string.section_app)) {
                SettingsItem(
                    icon = Icons.Default.Palette,
                    title = stringResource(R.string.theme),
                    subtitle = uiState.currentTheme,
                    onClick = { onToggleThemeDialog(true) }
                )
                SettingsItem(
                    icon = Icons.Default.Language,
                    title = stringResource(R.string.language),
                    subtitle = uiState.currentLanguage,
                    onClick = { onToggleLanguageDialog(true) }
                )
            }

            SettingsSection(title = stringResource(R.string.section_support)) {
                SettingsItem(
                    icon = Icons.AutoMirrored.Filled.Help,
                    title = stringResource(R.string.help),
                    onClick = { 
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Kidd-Mendo/pou-paw-appm"))
                        context.startActivity(intent)
                    }
                )
                SettingsItem(
                    icon = Icons.Default.Info,
                    title = stringResource(R.string.about),
                    onClick = { onToggleAboutDialog(true) }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onLogoutClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                )
            ) {
                Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(stringResource(R.string.logout))
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // Dialogs
    if (uiState.showThemeDialog) {
        val themes = listOf(
            stringResource(R.string.theme_light),
            stringResource(R.string.theme_dark),
            stringResource(R.string.theme_system)
        )
        SingleSelectDialog(
            title = stringResource(R.string.theme_dialog_title),
            options = themes,
            selectedOption = uiState.currentTheme,
            onOptionSelected = onSetTheme,
            onDismissRequest = { onToggleThemeDialog(false) }
        )
    }

    if (uiState.showLanguageDialog) {
        val languages = listOf(
            stringResource(R.string.lang_es),
            stringResource(R.string.lang_en)
        )
        SingleSelectDialog(
            title = stringResource(R.string.language_dialog_title),
            options = languages,
            selectedOption = uiState.currentLanguage,
            onOptionSelected = onSetLanguage,
            onDismissRequest = { onToggleLanguageDialog(false) }
        )
    }

    if (uiState.showAboutDialog) {
        AlertDialog(
            onDismissRequest = { onToggleAboutDialog(false) },
            title = { Text(stringResource(R.string.about_title)) },
            text = { Text(stringResource(R.string.about_description)) },
            confirmButton = {
                TextButton(onClick = { onToggleAboutDialog(false) }) {
                    Text(stringResource(R.string.close))
                }
            }
        )
    }
    
    if (uiState.showProfileDialog) {
        var name by remember { mutableStateOf(uiState.userName) }
        var email by remember { mutableStateOf(uiState.userEmail) }
        
        AlertDialog(
            onDismissRequest = { onToggleProfileDialog(false) },
            title = { Text(stringResource(R.string.dialog_edit_profile)) },
            text = {
                Column {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text(stringResource(R.string.name)) }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text(stringResource(R.string.email)) }
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { onUpdateProfile(name, email) }) {
                    Text(stringResource(R.string.save))
                }
            },
            dismissButton = {
                TextButton(onClick = { onToggleProfileDialog(false) }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }

    if (uiState.showPasswordDialog) {
        var password by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { onTogglePasswordDialog(false) },
            title = { Text(stringResource(R.string.change_password)) },
            text = {
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text(stringResource(R.string.password_label)) },
                    visualTransformation = PasswordVisualTransformation()
                )
            },
            confirmButton = {
                TextButton(onClick = { onChangePassword(password) }) {
                    Text(stringResource(R.string.save))
                }
            },
            dismissButton = {
                TextButton(onClick = { onTogglePasswordDialog(false) }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }
}

@Composable
fun SettingsSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            fontWeight = FontWeight.Bold
        )
        content()
    }
}

@Composable
fun SettingsItem(
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
    trailing: @Composable (() -> Unit)? = null,
    onClick: (() -> Unit)? = null
) {
    Surface(
        onClick = onClick ?: {},
        enabled = onClick != null,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge
                )
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }
            }
            if (trailing != null) {
                trailing()
            } else if (onClick != null) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                )
            }
        }
    }
}

@Composable
fun SingleSelectDialog(
    title: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    onDismissRequest: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(title) },
        text = {
            Column {
                options.forEach { option ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onOptionSelected(option) }
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (option == selectedOption),
                            onClick = { onOptionSelected(option) }
                        )
                        Text(
                            text = option,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismissRequest) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}
