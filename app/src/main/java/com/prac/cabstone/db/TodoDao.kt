package com.softsquared.myapplication.db

import androidx.room.*

@Dao
interface TodoDao {

    @Query("SELECT * FROM Todo ")
    fun getAll(): List<Todo>


    @Query("SELECT * FROM Todo WHERE day = :date and g_name = :g_name ORDER BY idx ASC")
    fun getDayList(date: String, g_name: String): List<Todo>

    @Query("SELECT IFNULL(MAX(gid) + 1, 1) FROM Todo")
    fun getNewGid(): Long

    @Query("SELECT COUNT(*) FROM Todo WHERE gid = :gid")
    fun getMyGroupSize(gid: Long): Long

    @Query("DELETE FROM Todo WHERE gid = :gid")
    fun removeGroup(gid: Long)

    @Query("SELECT IFNULL(MAX(idx) + 1, 1) FROM Todo WHERE day = :date")
    fun getIdx(date: String): Long

    @Insert
    fun insert(todo: Todo)

    @Update
    fun update(todo: Todo)

    @Delete
    fun delete(todo: Todo)
}