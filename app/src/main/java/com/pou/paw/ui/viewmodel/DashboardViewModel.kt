package com.pou.paw.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pou.paw.data.model.PouEntity
import com.pou.paw.data.repository.IPetPlantRepository
import com.pou.paw.data.repository.ISettingsRepository
import com.pou.paw.data.repository.IStatsRepository
import com.pou.paw.domain.usecase.FilterEntitiesUseCase
import com.pou.paw.domain.usecase.UpdateSatisfactionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
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
    private val petPlantRepository: IPetPlantRepository,
    private val settingsRepository: ISettingsRepository,
    private val statsRepository: IStatsRepository,
    private val filterEntitiesUseCase: FilterEntitiesUseCase,
    private val updateSatisfactionUseCase: UpdateSatisfactionUseCase
) : ViewModel() {

    private val _selectedFilter = MutableStateFlow("Todos")
    
    // Combinamos mascotas y plantas para mostrar en el Dashboard
    private val allEntities = petPlantRepository.petPlants

    val uiState: StateFlow<DashboardUiState> = combine(
        settingsRepository.userName,
        _selectedFilter,
        allEntities
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

    fun completeTask(entity: PouEntity, needName: String) {
        viewModelScope.launch {
            updateSatisfactionUseCase(entity, needName)
            statsRepository.incrementTasks()
        }
    }
}
