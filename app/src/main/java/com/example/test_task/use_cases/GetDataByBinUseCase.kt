package com.example.test_task.use_cases

import com.example.test_task.data.DataRepository
import com.example.test_task.data.ServerData

class GetDataByBinUseCase(val dataRepository: DataRepository) {
    suspend operator fun invoke(bin: String): ServerData {
        return dataRepository.getData(bin)
    }
}