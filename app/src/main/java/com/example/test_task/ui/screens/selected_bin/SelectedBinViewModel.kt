package com.example.test_task.ui.screens.selected_bin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.test_task.use_cases.GetDataByBinUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SelectedBinViewModel(
    val bin: String,
    val getDataByBinUseCase: GetDataByBinUseCase
) : ViewModel() {

    private val mutableState: MutableStateFlow<SelectedBinViewState> =
        MutableStateFlow(SelectedBinViewState())
    val viewState: StateFlow<SelectedBinViewState> = mutableState.asStateFlow()

    val coroutineExceptionHandler = CoroutineExceptionHandler() { coroutineContext, throwable ->
        mutableState.update {
            it.copy(
                state = SelectedBinViewState.State.ERROR
            )
        }
    }

    init {
        viewModelScope.launch(coroutineExceptionHandler) {
            val serverData = getDataByBinUseCase(bin)
            mutableState.update {
                it.copy(
                    scheme = serverData.scheme,
                    type = serverData.type,
                    brand = serverData.brand,
                    prepaid = serverData.prepaid,
                    country = SelectedBinViewState.Country(
                        name = serverData.country.name,
                        latitude = serverData.country.latitude,
                        longitude = serverData.country.longitude
                    ),
                    bank = SelectedBinViewState.Bank(
                        name = serverData.bank.name,
                        url = serverData.bank.url,
                        phone = serverData.bank.phone,
                        city = serverData.bank.city
                    ),
                    state = SelectedBinViewState.State.SUCCESS
                )
            }
        }
    }
}