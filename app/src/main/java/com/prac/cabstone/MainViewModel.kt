package com.prac.cabstone

import android.app.Application
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.AndroidViewModel
import androidx.room.Room
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.softsquared.myapplication.db.AppDatabase
import com.softsquared.myapplication.db.Groups
import com.softsquared.myapplication.db.Todo
import java.util.*

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val db = Room.databaseBuilder(
        application,
        AppDatabase::class.java, "todo.db"
    )
        .fallbackToDestructiveMigration()
        .allowMainThreadQueries()
        .build()
    private val base_cal = BaseCalendar()
    private val cal = base_cal.calendar
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var transaction: FragmentTransaction
    private var mainActivity: MainActivity? = null

    fun removeGroup(rm: Groups){
        return db.groupsDao().delete(rm)
    }

    fun getGroupIdx(): Long{
        return db.groupsDao().getIdx()
    }

    fun getTodoIdx(date: String): Long{
        return db.todoDao().getIdx(date)
    }

    fun getMainActivity(): MainActivity? {
        return mainActivity
    }

    fun setActivity(activity: MainActivity){
        mainActivity = activity
    }

    fun setTransaction(transaction: FragmentTransaction): Unit{
        this.transaction = transaction
    }

    fun setTransaction(): Unit{
        this.transaction = mainActivity?.supportFragmentManager?.beginTransaction()!!
    }

    fun getTransaction(): FragmentTransaction{
        return this.transaction
    }

    fun showTransaction(fragment: Fragment): Unit{
        transaction.show(fragment).commit()
    }

    fun addTransaction(fragment: Fragment): Unit{
        transaction.add(R.id.main_frame_layout, fragment)
    }

    fun removeTransaction(fragment: Fragment): Unit{
        transaction.remove(fragment)
    }

    fun hideTransaction(fragment: Fragment): Unit{
        transaction.hide(fragment).commit()
    }

    fun getCalendar(): Calendar {
        return cal
    }

    fun getBaseCalendar(): BaseCalendar {
        return base_cal
    }

    fun setBottomNavigationView(bottomNavigationView: BottomNavigationView) {
        this.bottomNavigationView = bottomNavigationView
    }

    fun getBottomNavigationView(): BottomNavigationView {
        return this.bottomNavigationView
    }

    fun getAll(): List<Todo>? {
        return db.todoDao().getAll()
    }

    fun getAllGList(): List<Groups>?{
        return db.groupsDao().getAll()
    }

    fun insert(todo: Todo) {
        db.todoDao().insert(todo)
    }

    fun insert(groups: Groups){
        db.groupsDao().insert(groups)
    }

    fun update(todo: Todo) {
        db.todoDao().update(todo)
    }

    fun update(groups: Groups){
        db.groupsDao().update(groups)
    }

    fun delete(todo: Todo) {
        db.todoDao().delete(todo)
    }

    fun delete(groups: Groups){
        db.groupsDao().delete(groups)
    }

    fun getDayList(date: String, g_name: String): List<Todo> {
        return db.todoDao().getDayList(date, g_name)
    }

    fun getNewGid(): Long {
        return db.todoDao().getNewGid()
    }

    fun getMyGroupSize(gid: Long): Long {
        return db.todoDao().getMyGroupSize(gid)
    }

    fun removeGroup(gid: Long) {
        db.todoDao().removeGroup(gid)
    }
}