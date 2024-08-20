package com.sps.data.repository

import com.sps.data.local.room.entity.Task
import com.sps.data.local.room.repository.LocalRepository
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val localRepository: LocalRepository
){
    suspend fun addTask(task: Task) = localRepository.addTask(task)
    suspend fun getAllTasks() = localRepository.getAllTasks()
}