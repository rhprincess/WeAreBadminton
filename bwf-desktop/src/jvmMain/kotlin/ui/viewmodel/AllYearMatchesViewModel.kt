package ui.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ui.uistate.AllYearMatchesUiState
import ui.uistate.LiveMatchUiState

class AllYearMatchesViewModel {
    private val _uiState = MutableStateFlow(AllYearMatchesUiState())
    val uiState: StateFlow<AllYearMatchesUiState> = _uiState.asStateFlow()

    companion object {
        @Composable
        fun viewModel(): AllYearMatchesViewModel = remember { AllYearMatchesViewModel() }
    }
}