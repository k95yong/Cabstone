package com.softsquared.myapplication.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Todo(
    var contents: String,
    var clear: Boolean = false,
    var day: String = "",
    var gid: Long = -1,
    var g_name: String = "",
    var idx: Long = 0,
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
)