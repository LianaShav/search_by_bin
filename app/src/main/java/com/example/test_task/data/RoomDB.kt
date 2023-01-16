package com.example.test_task.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(version = 1, entities = [BinEntity::class])
abstract class RoomDB:RoomDatabase() {
    abstract fun binDao():BinDao
}