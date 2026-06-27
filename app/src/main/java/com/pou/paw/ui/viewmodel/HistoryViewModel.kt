package com.pou.paw.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pou.paw.data.model.UserStats
import com.pou.paw.data.repository.IStatsRepository
import kotlinx.coroutines.flow.*

data class HistoryUiState(
    val stats: UserStats = UserStats(),
    val isLoading: Boolean = false
)

class HistoryViewModel(
    private val statsRepository: IStatsRepository
) : ViewModel() {

    val uiState: StateFlow<HistoryUiState> = statsRepository.userStats
        .map { HistoryUiState(stats = it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HistoryUiState(isLoading = true)
        )
}
