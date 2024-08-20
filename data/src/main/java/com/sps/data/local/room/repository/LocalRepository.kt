package com.sps.data.local.room.repository

import com.sps.data.local.room.entity.Task
import kotlinx.coroutines.flow.Flow

interface LocalRepository {
    suspend fun addTask(task: Task)
    suspend fun getAllTasks(): Flow<List<Task>>
}