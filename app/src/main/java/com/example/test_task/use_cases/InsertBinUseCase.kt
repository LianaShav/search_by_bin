package com.example.test_task.use_cases

import com.example.test_task.data.BinRepository

class InsertBinUseCase(val binRepository: BinRepository) {
    suspend operator fun invoke(bin : String) {
       binRepository.insert(bin)
    }
}