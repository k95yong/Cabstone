package com.softsquared.myapplication.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Groups(
    var gname: String,
    var idx: Long,
    var img_path: String,
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
)