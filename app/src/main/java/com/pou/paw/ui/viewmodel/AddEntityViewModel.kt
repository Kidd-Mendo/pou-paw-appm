package com.pou.paw.ui.viewmodel

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pou.paw.domain.usecase.SaveReminderUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate

data class AddEntityUiState(
    val name: String = "",
    val breedOrType: String = "",
    val selectedCategory: String = "Mascota",
    val imageUri: Uri? = null,
    val selectedAction: String = "Comida",
    val expandedAction: Boolean = false,
    val selectedFrequencyType: String = "Diario",
    val frequencyValue: Float = 1f,
    val selectedDate: LocalDate = LocalDate.now(),
    val message: String = ""
)

class AddEntityViewModel(
    private val saveReminderUseCase: SaveReminderUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddEntityUiState())
    val uiState: StateFlow<AddEntityUiState> = _uiState.asStateFlow()

    private val _saveSuccess = MutableSharedFlow<Unit>()
    val saveSuccess: SharedFlow<Unit> = _saveSuccess.asSharedFlow()

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
                imageUri = state.imageUri?.toString(),
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
