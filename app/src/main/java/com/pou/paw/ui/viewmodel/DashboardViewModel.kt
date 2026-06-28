package com.pou.paw.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pou.paw.data.model.PouEntity
import com.pou.paw.data.repository.IPetPlantRepository
import com.pou.paw.data.repository.IReminderRepository
import com.pou.paw.data.repository.ISettingsRepository
import com.pou.paw.data.repository.IStatsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import com.pou.paw.domain.usecase.FilterEntitiesUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DashboardUiState(
    val userName: String = "Usuario",
    val selectedFilter: String = "Todos",
    val items: List<PouEntity> = emptyList(),
    val isLoading: Boolean = false
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val reminderRepository: IReminderRepository,
    private val petPlantRepository: IPetPlantRepository,
    private val settingsRepository: ISettingsRepository,
    private val statsRepository: IStatsRepository,
    private val filterEntitiesUseCase: FilterEntitiesUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _selectedFilter = MutableStateFlow("Todos")
    
    val uiState: StateFlow<DashboardUiState> = combine(
        settingsRepository.userName,
        _selectedFilter,
        petPlantRepository.petPlants
    ) { name, filter, allItems ->
        DashboardUiState(
            userName = name,
            selectedFilter = filter,
            items = filterEntitiesUseCase(allItems, filter),
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DashboardUiState(isLoading = true)
    )

    fun updateFilter(filter: String) {
        _selectedFilter.value = filter
    }

    fun completeTask(entity: PouEntity) {
        // Lógica para completar tarea
    }
}
