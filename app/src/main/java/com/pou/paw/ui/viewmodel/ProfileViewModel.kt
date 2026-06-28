package com.pou.paw.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pou.paw.data.repository.IPetPlantRepository
import com.pou.paw.data.repository.IReminderRepository
import com.pou.paw.data.repository.ISettingsRepository
import com.pou.paw.domain.usecase.GetEntityCountsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

data class ProfileUiState(
    val name: String = "",
    val email: String = "",
    val registrationDate: String = "1 de Enero, 2024",
    val petCount: Int = 0,
    val plantCount: Int = 0,
    val activeRemindersCount: Int = 0
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val reminderRepository: IReminderRepository,
    private val petPlantRepository: IPetPlantRepository,
    private val settingsRepository: ISettingsRepository,
    private val getEntityCountsUseCase: GetEntityCountsUseCase
) : ViewModel() {

    val uiState: StateFlow<ProfileUiState> = combine(
        settingsRepository.userName,
        settingsRepository.userEmail,
        petPlantRepository.petPlants,
        reminderRepository.reminders
    ) { name, email, entities, reminders ->
        val (pets, plants) = getEntityCountsUseCase(entities)
        ProfileUiState(
            name = name,
            email = email,
            petCount = pets,
            plantCount = plants,
            activeRemindersCount = reminders.size
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ProfileUiState()
    )
}
