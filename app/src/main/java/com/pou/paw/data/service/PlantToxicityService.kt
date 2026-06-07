package com.pou.paw.data.service

object PlantToxicityService {
    private val toxicPlants = listOf("Lirio", "Azalea", "Filodendro", "Aloe Vera", "Helecho") // Example list

    fun isToxic(plantName: String): Boolean {
        return toxicPlants.any { it.equals(plantName, ignoreCase = true) }
    }

    fun getSafetyWarning(plantName: String): String? {
        return if (isToxic(plantName)) {
            "¡Advertencia! El $plantName es tóxico para perros y gatos. Mantenlo fuera de su alcance."
        } else null
    }
}
