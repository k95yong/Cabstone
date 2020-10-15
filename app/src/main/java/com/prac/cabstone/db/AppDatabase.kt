package com.softsquared.myapplication.db

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [Todo::class, Groups::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao
    abstract fun groupsDao(): GroupsDao
}