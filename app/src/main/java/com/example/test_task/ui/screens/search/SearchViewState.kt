package com.example.test_task.ui.screens.search

data class SearchViewState(
    val hasError: Boolean = false,
    val events : List<Event> = emptyList(),
    val binList: List<String> = emptyList()
)  {
    sealed class Event{
        data class GoToSelectedBin(
            val bin:String
        ):Event()
    }
}