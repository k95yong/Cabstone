package com.softsquared.myapplication.db

import androidx.room.*

@Dao
interface GroupsDao {

    @Query("SELECT * FROM Groups ORDER BY idx ASC")
    fun getAll(): List<Groups>

    @Query("SELECT IFNULL(MAX(idx), 1) FROM Groups")
    fun getIdx(): Long

    @Insert
    fun insert(groups: Groups)

    @Update
    fun update(groups: Groups)

    @Delete
    fun delete(groups: Groups)

    @Query("SELECT id FROM Groups WHERE gname = :group_name")
    fun getGidByName(group_name: String): Long
}