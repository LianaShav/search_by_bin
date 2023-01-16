package com.example.test_task.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.test_task.ui.screens.selected_bin.SelectedBinViewState

@Entity
data class BinEntity(
    @PrimaryKey
    val bin: String
)
