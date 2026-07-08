package com.pou.paw.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.pou.paw.PouPawApplication
import com.pou.paw.R
import com.pou.paw.data.model.ReminderEntity
import com.pou.paw.ui.theme.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.UUID

import com.pou.paw.ui.viewmodel.AddEntityViewModel
import com.pou.paw.ui.viewmodel.AddEntityUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEntityScreen(
    uiState: AddEntityUiState,
    onNameChange: (String) -> Unit,
    onBreedChange: (String) -> Unit,
    onCategoryChange: (String) -> Unit,
    onImageUriChange: (Uri?) -> Unit,
    onActionChange: (String) -> Unit,
    onExpandedActionChange: (Boolean) -> Unit,
    onFrequencyTypeChange: (String) -> Unit,
    onFrequencyValueChange: (Float) -> Unit,
    onDateChange: (LocalDate) -> Unit,
    onMessageChange: (String) -> Unit,
    onFetchRandomImage: () -> Unit,
    onSaveReminder: () -> Unit,
    onBack: () -> Unit
) {
    val nameFocusRequester = remember { FocusRequester() }
    val breedFocusRequester = remember { FocusRequester() }
    
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        onImageUriChange(uri)
    }

    val petActions = listOf("Comida", "Agua", "Pasear", "Limpieza", "Medicina")
    val plantActions = listOf("Regar", "Sacar al sol", "Nutrientes", "Limpiar hojas", "Trasplantar")

    val nextDays = remember {
        (0..4).map { LocalDate.now().plusDays(it.toLong()) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    TextButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBackIos, null, modifier = Modifier.size(16.dp), tint = DarkOlive)
                        Text(stringResource(R.string.back_label), color = DarkOlive, fontWeight = FontWeight.Bold)
                    }
                },
                actions = {
                    Text(
                        stringResource(R.string.new_reminder_title),
                        modifier = Modifier.padding(end = 16.dp),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = DarkOlive
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CreamBackground)
            )
        },
        containerColor = CreamBackground
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            // Header Section
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = Color.White.copy(alpha = 0.6f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(110.dp)
                            .clip(RoundedCornerShape(22.dp))
                            .background(LightSage)
                            .clickable { launcher.launch("image/*") },
                        contentAlignment = Alignment.Center
                    ) {
                        val displayImage = uiState.imageUri ?: uiState.networkImageUrl
                        if (displayImage != null) {
                            AsyncImage(
                                model = displayImage,
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Default.AddAPhoto,
                                    contentDescription = stringResource(R.string.add_photo),
                                    tint = OliveGreen,
                                    modifier = Modifier.size(40.dp)
                                )
                                Text(stringResource(R.string.add_photo), fontSize = 11.sp, color = OliveGreen, fontWeight = FontWeight.Bold)
                            }
                        }
                        
                        if (uiState.isLoadingNetwork) {
                            Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.3f)), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.width(16.dp))
                    
                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically, 
                            modifier = Modifier.clickable { nameFocusRequester.requestFocus() }
                        ) {
                            Box(contentAlignment = Alignment.CenterStart, modifier = Modifier.weight(1f, fill = false)) {
                                if (uiState.name.isEmpty()) {
                                    Text(stringResource(R.string.write_name_placeholder), style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold, color = TextGray.copy(alpha = 0.5f)))
                                }
                                BasicTextField(
                                    value = uiState.name,
                                    onValueChange = onNameChange,
                                    textStyle = MaterialTheme.typography.headlineSmall.copy(
                                        fontWeight = FontWeight.Bold, 
                                        color = TextBlack
                                    ),
                                    modifier = Modifier.focusRequester(nameFocusRequester),
                                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                                )
                            }
                            IconButton(onClick = { nameFocusRequester.requestFocus() }, modifier = Modifier.size(32.dp)) {
                                Icon(Icons.Default.Edit, null, tint = OliveGreen, modifier = Modifier.size(24.dp))
                            }
                        }
                        
                        Row(
                            verticalAlignment = Alignment.CenterVertically, 
                            modifier = Modifier.padding(top = 2.dp).clickable { breedFocusRequester.requestFocus() }
                        ) {
                            Box(contentAlignment = Alignment.CenterStart, modifier = Modifier.weight(1f, fill = false)) {
                                if (uiState.breedOrType.isEmpty()) {
                                    Text(
                                        if (uiState.selectedCategory == "Mascota") stringResource(R.string.pet_breed_placeholder) else stringResource(R.string.plant_type_placeholder),
                                        style = MaterialTheme.typography.bodyMedium.copy(color = TextGray.copy(alpha = 0.5f))
                                    )
                                }
                                BasicTextField(
                                    value = uiState.breedOrType,
                                    onValueChange = onBreedChange,
                                    textStyle = MaterialTheme.typography.bodyMedium.copy(color = TextBlack),
                                    modifier = Modifier.focusRequester(breedFocusRequester),
                                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
                                )
                            }
                            IconButton(onClick = { breedFocusRequester.requestFocus() }, modifier = Modifier.size(28.dp)) {
                                Icon(Icons.Default.Edit, null, tint = OliveGreen, modifier = Modifier.size(20.dp))
                            }
                        }
                        
                        Icon(
                            imageVector = if (uiState.selectedCategory == "Mascota") Icons.Default.Pets else Icons.Default.Eco,
                            null,
                            tint = OliveGreen,
                            modifier = Modifier.padding(top = 8.dp).size(32.dp)
                        )
                        
                        if (uiState.selectedCategory == "Mascota") {
                            TextButton(
                                onClick = onFetchRandomImage,
                                modifier = Modifier.padding(top = 4.dp),
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.CloudDownload, null, modifier = Modifier.size(16.dp), tint = OliveGreen)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        stringResource(R.string.get_network_image),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = OliveGreen
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Categoría
            Text(stringResource(R.string.category_label), fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = { onCategoryChange("Mascota") },
                    modifier = Modifier.weight(1f).height(45.dp),
                    shape = RoundedCornerShape(22.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (uiState.selectedCategory == "Mascota") DarkOlive else Color.White
                    )
                ) {
                    Text(stringResource(R.string.category_pet), color = if (uiState.selectedCategory == "Mascota") Color.White else DarkOlive)
                }
                Button(
                    onClick = { onCategoryChange("Planta") },
                    modifier = Modifier.weight(1f).height(45.dp),
                    shape = RoundedCornerShape(22.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (uiState.selectedCategory == "Planta") DarkOlive else Color.White
                    )
                ) {
                    Text(stringResource(R.string.category_plant), color = if (uiState.selectedCategory == "Planta") Color.White else DarkOlive)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Acción
            Text(stringResource(R.string.action_label), fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))
            ExposedDropdownMenuBox(
                expanded = uiState.expandedAction,
                onExpandedChange = onExpandedActionChange,
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = uiState.selectedAction,
                    onValueChange = { },
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth().menuAnchor(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White,
                        unfocusedTextColor = TextBlack,
                        focusedTextColor = TextBlack,
                        unfocusedLabelColor = TextGray,
                        focusedLabelColor = DarkOlive
                    ),
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = uiState.expandedAction) }
                )
                ExposedDropdownMenu(
                    expanded = uiState.expandedAction,
                    onDismissRequest = { onExpandedActionChange(false) },
                    modifier = Modifier.background(Color.White)
                ) {
                    val currentActions = if (uiState.selectedCategory == "Mascota") petActions else plantActions
                    currentActions.forEach { action ->
                        DropdownMenuItem(
                            text = { Text(action) },
                            onClick = {
                                onActionChange(action)
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 4. Frecuencia
            Text(stringResource(R.string.frequency_label), fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                nextDays.forEach { date ->
                    FrequencyDayItem(
                        date = date,
                        isSelected = uiState.selectedDate == date,
                        onClick = { onDateChange(date) }
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                FrequencyTypeOption(stringResource(R.string.frequency_daily), uiState.selectedFrequencyType == "Diario") { onFrequencyTypeChange("Diario") }
                FrequencyTypeOption(stringResource(R.string.frequency_hours), uiState.selectedFrequencyType == "Cada X Horas") { onFrequencyTypeChange("Cada X Horas") }
                FrequencyTypeOption(stringResource(R.string.frequency_days), uiState.selectedFrequencyType == "Cada X Días") { onFrequencyTypeChange("Cada X Días") }
            }
            
            Slider(
                value = uiState.frequencyValue,
                onValueChange = onFrequencyValueChange,
                valueRange = if (uiState.selectedFrequencyType == "Cada X Días") 1f..30f else 1f..24f,
                colors = SliderDefaults.colors(
                    thumbColor = Color.White,
                    activeTrackColor = DarkOlive,
                    inactiveTrackColor = ChipUnselected
                )
            )
            
            val frequencyText = when(uiState.selectedFrequencyType) {
                "Diario" -> stringResource(R.string.frequency_once_daily)
                "Cada X Horas" -> stringResource(R.string.frequency_every_hours, uiState.frequencyValue.toInt())
                "Cada X Días" -> stringResource(R.string.frequency_every_days, uiState.frequencyValue.toInt())
                else -> ""
            }
            Text(frequencyText, modifier = Modifier.align(Alignment.End), fontWeight = FontWeight.Bold, color = DarkOlive)

            Spacer(modifier = Modifier.height(24.dp))

            // Mensaje
            Text(stringResource(R.string.message_label), fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = uiState.message,
                onValueChange = onMessageChange,
                placeholder = { Text(stringResource(R.string.message_placeholder)) },
                modifier = Modifier.fillMaxWidth().height(100.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White.copy(alpha = 0.5f),
                    focusedContainerColor = Color.White.copy(alpha = 0.5f),
                    unfocusedTextColor = TextBlack,
                    focusedTextColor = TextBlack,
                    unfocusedPlaceholderColor = TextGray.copy(alpha = 0.6f),
                    focusedPlaceholderColor = TextGray.copy(alpha = 0.6f)
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Botones
            Button(
                onClick = onSaveReminder,
                modifier = Modifier.fillMaxWidth().height(55.dp),
                shape = RoundedCornerShape(27.dp),
                colors = ButtonDefaults.buttonColors(containerColor = OliveGreen)
            ) {
                Text(stringResource(R.string.create_reminder_button), fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
            
            TextButton(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.cancel_button), color = TextGray, fontWeight = FontWeight.Bold)
            }
        }
    }
}


@Composable
fun FrequencyDayItem(date: LocalDate, isSelected: Boolean, onClick: () -> Unit) {
    val dayFormatter = DateTimeFormatter.ofPattern("EEE", Locale("es", "ES"))
    val dateFormatter = DateTimeFormatter.ofPattern("dd")
    
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) DarkOlive else Color.White.copy(alpha = 0.4f),
        modifier = Modifier.width(60.dp).height(70.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = date.format(dayFormatter).replaceFirstChar { it.uppercase() }.take(3), 
                fontSize = 12.sp, 
                color = if (isSelected) Color.White else TextGray
            )
            Text(
                text = date.format(dateFormatter), 
                fontSize = 20.sp, 
                fontWeight = FontWeight.Bold, 
                color = if (isSelected) Color.White else TextBlack
            )
        }
    }
}

@Composable
fun FrequencyTypeOption(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Text(
        text = text,
        fontSize = 12.sp,
        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
        color = if (isSelected) DarkOlive else TextGray,
        modifier = Modifier.clickable { onClick() }
    )
}
