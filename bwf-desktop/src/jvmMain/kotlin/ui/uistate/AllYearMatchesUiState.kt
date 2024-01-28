package ui.uistate

import data.bean.MonthState

data class AllYearMatchesUiState(
    val year: String = "2024",
    val yearMatchesResults: LinkedHashMap<String, List<MonthState>> = linkedMapOf()
)