package com.pou.paw.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import com.pou.paw.R
import com.pou.paw.PouPawApplication
import com.pou.paw.data.model.Pet
import com.pou.paw.data.model.Plant
import com.pou.paw.ui.theme.*

@Composable
fun DashboardScreen(onAddClick: () -> Unit, onSettingsClick: () -> Unit) {
    val context = LocalContext.current
    val repository = (context.applicationContext as PouPawApplication).repository
    val reminders by repository.reminders.collectAsState(initial = emptyList())
    
    var selectedFilter by remember { mutableStateOf("Todos") }
    
    // Mock inicial
    val initialItems = listOf(
        Pet(name = "Luna", type = "Gato", breed = "Gato"),
        Plant(name = "Helecho", type = "Planta", species = "Helecho")
    )

    Scaffold(
        bottomBar = { BottomNavBar(onAddClick, onSettingsClick) },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            HeaderSection()
            Spacer(modifier = Modifier.height(24.dp))
            FilterSection(selectedFilter) { selectedFilter = it }
            Spacer(modifier = Modifier.height(20.dp))
            
            val filteredItems = (initialItems + reminders.map { reminder ->
                if (reminder.category == "Mascota") {
                    Pet(name = reminder.targetId, type = reminder.action, breed = reminder.frequency, imageUrl = reminder.imageUri)
                } else {
                    Plant(name = reminder.targetId, type = reminder.action, species = reminder.frequency, imageUrl = reminder.imageUri)
                }
            }).filter { item ->
                when (selectedFilter) {
                    stringResource(R.string.filter_pets) -> item is Pet
                    stringResource(R.string.filter_plants) -> item is Plant
                    else -> true
                }
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(20.dp),
                contentPadding = PaddingValues(bottom = 20.dp)
            ) {
                items(filteredItems) { item ->
                    when (item) {
                        is Pet -> PetCard(pet = item)
                        is Plant -> PlantCard(plant = item)
                    }
                }
            }
        }
    }
}

@Composable
fun HeaderSection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Pets,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Pou-Paw",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold
            )
        }
        Box(
            modifier = Modifier
                .size(45.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.secondaryContainer)
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                modifier = Modifier.align(Alignment.Center),
                tint = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
    Text(
        text = stringResource(R.string.welcome_user, "Ana"),
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.ExtraBold,
        color = MaterialTheme.colorScheme.onBackground
    )
    Text(
        text = stringResource(R.string.dashboard_subtitle),
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
    )
}

@Composable
fun FilterSection(selected: String, onFilterSelected: (String) -> Unit) {
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        val all = stringResource(R.string.filter_all)
        val pets = stringResource(R.string.filter_pets)
        val plants = stringResource(R.string.filter_plants)
        FilterItem(all, Icons.Default.Apps, selected == all) { onFilterSelected(all) }
        FilterItem(pets, Icons.Default.Pets, selected == pets) { onFilterSelected(pets) }
        FilterItem(plants, Icons.Default.Eco, selected == plants) { onFilterSelected(plants) }
    }
}

@Composable
fun FilterItem(text: String, icon: ImageVector, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondaryContainer,
        modifier = Modifier.height(40.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = text,
                color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondaryContainer,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun PetCard(pet: Pet) {
    Card(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                ) {
                    Icon(
                        imageVector = Icons.Default.Pets,
                        contentDescription = null,
                        modifier = Modifier.align(Alignment.Center).size(40.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                        Text(text = pet.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                        Icon(Icons.Default.Pets, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                    }
                    Text(text = pet.type, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Schedule, null, tint = ProgressRed, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = pet.breed, color = ProgressRed, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row(horizontalArrangement = Arrangement.SpaceAround, modifier = Modifier.fillMaxWidth()) {
                NeedIndicator(stringResource(R.string.need_food), 0.4f, Icons.Default.Restaurant, ProgressRed)
                NeedIndicator(stringResource(R.string.need_water), 0.8f, Icons.Default.WaterDrop, ProgressBlue)
                NeedIndicator(stringResource(R.string.need_cleaning), 0.2f, Icons.Default.CleaningServices, ProgressGreen)
            }
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = { },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(25.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
            ) {
                Text(stringResource(R.string.action_feed), fontWeight = FontWeight.Bold, fontSize = 16.sp, color = MaterialTheme.colorScheme.onTertiary)
            }
        }
    }
}

@Composable
fun PlantCard(plant: Plant) {
    Card(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                ) {
                    Icon(
                        imageVector = Icons.Default.Eco,
                        contentDescription = null,
                        modifier = Modifier.align(Alignment.Center).size(40.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                        Text(text = plant.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                        Icon(Icons.Default.Eco, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                    }
                    Text(text = plant.species, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CheckCircle, null, tint = ProgressGreen, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = stringResource(R.string.status_satisfied), color = ProgressGreen, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row(horizontalArrangement = Arrangement.SpaceAround, modifier = Modifier.fillMaxWidth()) {
                NeedIndicator(stringResource(R.string.need_water), 0.75f, Icons.Default.WaterDrop, ProgressBlue)
                NeedIndicator(stringResource(R.string.need_light), 0.9f, Icons.Default.WbSunny, ProgressYellow)
                NeedIndicator(stringResource(R.string.need_nutrient), 0.4f, Icons.Default.Grass, ProgressGreen)
            }
        }
    }
}

@Composable
fun NeedIndicator(name: String, level: Float, icon: ImageVector, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(55.dp)) {
            CircularProgressIndicator(
                progress = level,
                modifier = Modifier.fillMaxSize(),
                strokeWidth = 6.dp,
                color = color,
                trackColor = MaterialTheme.colorScheme.secondaryContainer
            )
            Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(24.dp))
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = name, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
    }
}

@Composable
fun BottomNavBar(onAddClick: () -> Unit, onSettingsClick: () -> Unit) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp,
        modifier = Modifier.clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Dashboard, contentDescription = null) },
            label = { Text(stringResource(R.string.nav_dashboard)) },
            selected = true,
            onClick = { },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                indicatorColor = MaterialTheme.colorScheme.secondaryContainer
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.AddCircleOutline, contentDescription = null) },
            label = { Text(stringResource(R.string.nav_add)) },
            selected = false,
            onClick = onAddClick,
            colors = NavigationBarItemDefaults.colors(
                unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Settings, contentDescription = null) },
            label = { Text(stringResource(R.string.settings_title)) },
            selected = false,
            onClick = onSettingsClick,
            colors = NavigationBarItemDefaults.colors(
                unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.PersonOutline, contentDescription = null) },
            label = { Text(stringResource(R.string.nav_profile)) },
            selected = false,
            onClick = { },
            colors = NavigationBarItemDefaults.colors(
                unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
            )
        )
    }
}
