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
    var mapX: Double = 0.0,
    var mapY: Double = 0.0,
    var contentId: Int = 0,
    var addr1: String = "",
    var firstImage: String = "",
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
)