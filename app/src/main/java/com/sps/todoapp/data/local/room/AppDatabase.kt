package com.sps.todoapp.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sps.todoapp.data.local.room.dao.TaskDao
import com.sps.todoapp.data.local.room.entity.Task

@Database(entities = [Task::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}