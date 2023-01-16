package com.example.test_task.ui.screens.selected_bin

import kotlinx.serialization.Serializable

data class SelectedBinViewState(
    val scheme: String = "",
    val type: String = "",
    val brand: String = "",
    val prepaid: Boolean = false,
    val country: Country = Country(),
    val bank: Bank = Bank(),

    val state: State = State.LOADING
) {
    enum class State {
        SUCCESS,
        LOADING,
        ERROR
    }

    data class Bank(
        val name: String = "",
        val url: String = "",
        val phone: String = "",
        val city: String = "",
    )

    data class Country(
        val name: String = "",
        val latitude: Int = 0,
        val longitude: Int = 0,
    )
}

class A {
//    val selectedBinViewState = SelectedBinViewState()
//    fun f (){
//        selectedBinViewState.bank.name
//        selectedBinViewState.scheme
//        selectedBinViewState.bank.city
//    }
}
