package com.example.test_task.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface BinDao {
    @Insert(onConflict = REPLACE)
    suspend fun insert(bin: BinEntity): Long
    @Transaction
    @Query("SELECT * FROM BinEntity")
    suspend fun getBinList(): List<BinEntity>
}