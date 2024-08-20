package com.sps.todoapp.repository

import com.sps.todoapp.data.local.room.entity.Task
import com.sps.todoapp.data.local.room.repository.LocalRepository
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val localRepository: LocalRepository
){
    suspend fun addTask(task: Task) = localRepository.addTask(task)
    suspend fun getAllTasks() = localRepository.getAllTasks()
}