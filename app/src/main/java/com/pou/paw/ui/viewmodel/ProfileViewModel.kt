package com.pou.paw.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pou.paw.data.repository.IPetPlantRepository
import com.pou.paw.data.repository.IReminderRepository
import com.pou.paw.data.repository.ISettingsRepository
import com.pou.paw.domain.usecase.GetEntityCountsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUiState(
    val name: String = "",
    val email: String = "",
    val photoUri: String? = null,
    val registrationDate: String = "1 de Enero, 2024",
    val petCount: Int = 0,
    val plantCount: Int = 0,
    val activeRemindersCount: Int = 0,
    val isEditing: Boolean = false,
    val editName: String = "",
    val editEmail: String = ""
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val reminderRepository: IReminderRepository,
    private val petPlantRepository: IPetPlantRepository,
    private val settingsRepository: ISettingsRepository,
    private val getEntityCountsUseCase: GetEntityCountsUseCase
) : ViewModel() {

    private val _isEditing = MutableStateFlow(false)
    private val _editName = MutableStateFlow("")
    private val _editEmail = MutableStateFlow("")

    val uiState: StateFlow<ProfileUiState> = combine(
        settingsRepository.userName,
        settingsRepository.userEmail,
        settingsRepository.userPhotoUri,
        petPlantRepository.petPlants,
        reminderRepository.reminders,
        _isEditing,
        _editName,
        _editEmail
    ) { flows ->
        val name = flows[0] as String
        val email = flows[1] as String
        val photo = flows[2] as String?
        val entities = flows[3] as List<*>
        val reminders = flows[4] as List<*>
        val editing = flows[5] as Boolean
        val ename = flows[6] as String
        val eemail = flows[7] as String

        val (pets, plants) = getEntityCountsUseCase(entities as List<com.pou.paw.data.model.PouEntity>)
        ProfileUiState(
            name = name,
            email = email,
            photoUri = photo,
            petCount = pets,
            plantCount = plants,
            activeRemindersCount = reminders.size,
            isEditing = editing,
            editName = if (editing) ename else name,
            editEmail = if (editing) eemail else email
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ProfileUiState()
    )

    fun startEditing() {
        _editName.value = uiState.value.name
        _editEmail.value = uiState.value.email
        _isEditing.value = true
    }

    fun cancelEditing() {
        _isEditing.value = false
    }

    fun onEditNameChange(newName: String) {
        _editName.value = newName
    }

    fun onEditEmailChange(newEmail: String) {
        _editEmail.value = newEmail
    }

    fun saveProfile() {
        viewModelScope.launch {
            settingsRepository.updateProfile(_editName.value, _editEmail.value)
            _isEditing.value = false
        }
    }

    fun updatePhoto(uri: String) {
        viewModelScope.launch {
            settingsRepository.updateProfile(uiState.value.name, uiState.value.email, uri)
        }
    }
}
