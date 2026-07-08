package com.pou.paw.ui.viewmodel

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pou.paw.domain.usecase.GetBreedsUseCase
import com.pou.paw.domain.usecase.GetRandomPetImageUseCase
import com.pou.paw.domain.usecase.SaveReminderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class AddEntityUiState(
    val name: String = "",
    val breedOrType: String = "",
    val breeds: List<String> = emptyList(),
    val selectedCategory: String = "Mascota",
    val imageUri: Uri? = null,
    val networkImageUrl: String? = null,
    val selectedAction: String = "Comida",
    val expandedAction: Boolean = false,
    val selectedFrequencyType: String = "Diario",
    val frequencyValue: Float = 1f,
    val selectedDate: LocalDate = LocalDate.now(),
    val message: String = "",
    val isLoadingNetwork: Boolean = false
)

@HiltViewModel
class AddEntityViewModel @Inject constructor(
    private val saveReminderUseCase: SaveReminderUseCase,
    private val getBreedsUseCase: GetBreedsUseCase,
    private val getRandomPetImageUseCase: GetRandomPetImageUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddEntityUiState())
    val uiState: StateFlow<AddEntityUiState> = _uiState.asStateFlow()

    private val _saveSuccess = MutableSharedFlow<Unit>()
    val saveSuccess: SharedFlow<Unit> = _saveSuccess.asSharedFlow()

    init {
        loadBreeds()
    }

    private fun loadBreeds() {
        viewModelScope.launch {
            val breeds = getBreedsUseCase()
            _uiState.update { it.copy(breeds = breeds) }
        }
    }

    fun fetchRandomImage() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingNetwork = true) }
            val breed = _uiState.value.breedOrType.lowercase().takeIf { it.isNotBlank() }
            val imageUrl = getRandomPetImageUseCase(breed)
            _uiState.update { it.copy(networkImageUrl = imageUrl, isLoadingNetwork = false) }
        }
    }

    fun onNameChange(name: String) {
        _uiState.update { it.copy(name = name) }
    }

    fun onBreedChange(breed: String) {
        _uiState.update { it.copy(breedOrType = breed) }
    }

    fun onCategoryChange(category: String) {
        _uiState.update { 
            it.copy(
                selectedCategory = category,
                selectedAction = if (category == "Mascota") "Comida" else "Regar"
            ) 
        }
    }

    fun onImageUriChange(uri: Uri?) {
        _uiState.update { it.copy(imageUri = uri) }
    }

    fun onActionChange(action: String) {
        _uiState.update { it.copy(selectedAction = action, expandedAction = false) }
    }

    fun onExpandedActionChange(expanded: Boolean) {
        _uiState.update { it.copy(expandedAction = expanded) }
    }

    fun onFrequencyTypeChange(type: String) {
        _uiState.update { it.copy(selectedFrequencyType = type) }
    }

    fun onFrequencyValueChange(value: Float) {
        _uiState.update { it.copy(frequencyValue = value) }
    }

    fun onDateChange(date: LocalDate) {
        _uiState.update { it.copy(selectedDate = date) }
    }

    fun onMessageChange(message: String) {
        _uiState.update { it.copy(message = message) }
    }

    fun saveReminder() {
        viewModelScope.launch {
            val state = _uiState.value
            saveReminderUseCase(
                name = state.name,
                category = state.selectedCategory,
                breedOrType = state.breedOrType,
                imageUri = state.imageUri?.toString() ?: state.networkImageUrl,
                action = state.selectedAction,
                frequencyType = state.selectedFrequencyType,
                frequencyValue = state.frequencyValue.toInt(),
                date = state.selectedDate,
                message = state.message
            )
            _saveSuccess.emit(Unit)
        }
    }
}
