package com.example.test_task.data

import kotlinx.serialization.Serializable

@Serializable
data class ServerData(
    val scheme: String = "",
    val type: String = "",
    val brand: String = "",
    val prepaid: Boolean = false,
    val country: Country = Country(),
    val bank: Bank = Bank(),
) {
    @Serializable
    data class Country(
        val name: String = "",
        val latitude: Int = 0,
        val longitude: Int = 0,
    )

    @Serializable
    data class Bank(
        val name: String = "",
        val url: String = "",
        val phone: String = "",
        val city: String = "",
    )
}
