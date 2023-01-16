package com.example.test_task.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.test_task.BIN_LENGTH
import com.example.test_task.data.RoomDB
import com.example.test_task.use_cases.GetBinListUseCase
import com.example.test_task.use_cases.InsertBinUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel(
   val insertBinUseCase: InsertBinUseCase,
   val getBinListUseCase: GetBinListUseCase
):ViewModel() {
   private val mutableState: MutableStateFlow<SearchViewState> =
      MutableStateFlow(SearchViewState())
   val viewState: StateFlow<SearchViewState> = mutableState.asStateFlow()

   fun update(){
      viewModelScope.launch {
         mutableState.update {
            it.copy(
               binList = getBinListUseCase()
            )
         }
      }
   }

   fun checkBin(bin:String){
      viewModelScope.launch {
         if (bin.isEmpty() || bin.length < BIN_LENGTH){
            mutableState.update {
               it.copy(
                  hasError = true
               )
            }
            return@launch
         }

         insertBinUseCase(bin)
         mutableState.update { state ->
            state.copy(
               events = state.events + SearchViewState.Event.GoToSelectedBin(bin)
            )
         }
      }
   }

   fun consumeEvents(events: List<SearchViewState.Event>) {
      mutableState.update { state ->
      state.copy(events = state.events - events.toSet())
      }
   }

}