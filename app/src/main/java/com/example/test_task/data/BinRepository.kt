package com.example.test_task.data

class BinRepository(val binDao: BinDao) {

    suspend fun insert(bin: String) {
        binDao.insert(BinEntity(bin))
    }
    suspend fun getBinList(): List<String>{
        return binDao.getBinList().map { it.bin }
    }
}