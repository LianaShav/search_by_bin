package com.example.test_task.data

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.serialization.json.Json

class DataRepository {
    val client = HttpClient()

    val json = Json {
        isLenient = false
        ignoreUnknownKeys = true
    }

    suspend fun getData(bin: String): ServerData {
        val url = "https://lookup.binlist.net/$bin"
        return json.decodeFromString(
            ServerData.serializer(), client.get(url).body()
        )
    }
}