package com.example.test_task.use_cases

import com.example.test_task.data.BinRepository

class GetBinListUseCase(val binRepository: BinRepository) {
    suspend operator fun invoke(): List<String> {
        return binRepository.getBinList().reversed()
    }
}