package com.sps.todoapp.data.local.room.repository

import com.sps.todoapp.data.local.room.dao.TaskDao
import com.sps.todoapp.data.local.room.entity.Task
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalRepositoryImpl @Inject constructor(
    private val taskDao: TaskDao): LocalRepository {
    override suspend fun addTask(task: Task) {
        taskDao.insertTasks(task)
    }

    override suspend fun getAllTasks(): Flow<List<Task>> {
        return taskDao.getAllTasks()
    }
}