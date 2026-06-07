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
import com.pou.paw.PouPawApplication
import com.pou.paw.data.model.Pet
import com.pou.paw.data.model.Plant
import com.pou.paw.ui.theme.*

@Composable
fun DashboardScreen(onAddClick: () -> Unit) {
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
        bottomBar = { BottomNavBar(onAddClick) },
        containerColor = CreamBackground
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
                    "Mascotas" -> item is Pet
                    "Plantas" -> item is Plant
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
                tint = OliveGreen,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Pou-Paw",
                style = MaterialTheme.typography.titleLarge,
                color = DarkOlive,
                fontWeight = FontWeight.Bold
            )
        }
        Box(
            modifier = Modifier
                .size(45.dp)
                .clip(CircleShape)
                .background(Color.LightGray)
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                modifier = Modifier.align(Alignment.Center),
                tint = Color.White
            )
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
    Text(
        text = "¡Hola, Ana!",
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.ExtraBold,
        color = TextBlack
    )
    Text(
        text = "Tus amigos te necesitan.",
        style = MaterialTheme.typography.bodyLarge,
        color = TextGray
    )
}

@Composable
fun FilterSection(selected: String, onFilterSelected: (String) -> Unit) {
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        FilterItem("Todos", Icons.Default.Apps, selected == "Todos") { onFilterSelected("Todos") }
        FilterItem("Mascotas", Icons.Default.Pets, selected == "Mascotas") { onFilterSelected("Mascotas") }
        FilterItem("Plantas", Icons.Default.Eco, selected == "Plantas") { onFilterSelected("Plantas") }
    }
}

@Composable
fun FilterItem(text: String, icon: ImageVector, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) DarkOlive else ChipUnselected,
        modifier = Modifier.height(40.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected) Color.White else DarkOlive,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = text,
                color = if (isSelected) Color.White else DarkOlive,
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
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(LightSage)
                ) {
                    Icon(
                        imageVector = Icons.Default.Pets,
                        contentDescription = null,
                        modifier = Modifier.align(Alignment.Center).size(40.dp),
                        tint = OliveGreen
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                        Text(text = pet.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Icon(Icons.Default.Pets, null, tint = OliveGreen, modifier = Modifier.size(20.dp))
                    }
                    Text(text = pet.type, style = MaterialTheme.typography.bodyMedium, color = TextGray)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Schedule, null, tint = Color.Red, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = pet.breed, color = Color.Red, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row(horizontalArrangement = Arrangement.SpaceAround, modifier = Modifier.fillMaxWidth()) {
                NeedIndicator("Comida", 0.4f, Icons.Default.Restaurant, ProgressRed)
                NeedIndicator("Agua", 0.8f, Icons.Default.WaterDrop, ProgressBlue)
                NeedIndicator("Limpieza", 0.2f, Icons.Default.CleaningServices, ProgressGreen)
            }
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = { },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(25.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ActionSalmon)
            ) {
                Text("Alimentar", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun PlantCard(plant: Plant) {
    Card(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(LightSage)
                ) {
                    Icon(
                        imageVector = Icons.Default.Eco,
                        contentDescription = null,
                        modifier = Modifier.align(Alignment.Center).size(40.dp),
                        tint = OliveGreen
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                        Text(text = plant.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Icon(Icons.Default.Eco, null, tint = OliveGreen, modifier = Modifier.size(20.dp))
                    }
                    Text(text = plant.species, style = MaterialTheme.typography.bodyMedium, color = TextGray)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CheckCircle, null, tint = ProgressGreen, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Satisfecho", color = ProgressGreen, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row(horizontalArrangement = Arrangement.SpaceAround, modifier = Modifier.fillMaxWidth()) {
                NeedIndicator("Agua", 0.75f, Icons.Default.WaterDrop, ProgressBlue)
                NeedIndicator("Luz", 0.9f, Icons.Default.WbSunny, ProgressYellow)
                NeedIndicator("Nutriente", 0.4f, Icons.Default.Grass, ProgressGreen)
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
                trackColor = ChipUnselected
            )
            Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(24.dp))
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = name, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextGray)
    }
}

@Composable
fun BottomNavBar(onAddClick: () -> Unit) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp,
        modifier = Modifier.clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Dashboard, contentDescription = null) },
            label = { Text("Dashboard") },
            selected = true,
            onClick = { },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = DarkOlive,
                selectedTextColor = DarkOlive,
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
                indicatorColor = LightSage
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.AddCircleOutline, contentDescription = null) },
            label = { Text("Añadir") },
            selected = false,
            onClick = onAddClick
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Settings, contentDescription = null) },
            label = { Text("Configuración") },
            selected = false,
            onClick = { }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.PersonOutline, contentDescription = null) },
            label = { Text("Perfil") },
            selected = false,
            onClick = { }
        )
    }
}
