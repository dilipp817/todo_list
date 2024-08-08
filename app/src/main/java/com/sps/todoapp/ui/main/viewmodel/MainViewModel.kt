package com.sps.todoapp.ui.main.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sps.todoapp.data.local.room.entity.Task
import com.sps.todoapp.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    private val _tasksStateFlow = MutableStateFlow<List<Task>>(emptyList())
    val tasksStateFlow: StateFlow<List<Task>> get() = _tasksStateFlow

    init {
        fetchTasks()
    }

    fun fetchTasks() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllTasks().collect { tasks ->
                _tasksStateFlow.value = tasks
            }
        }
    }

    fun addTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO ) {
            repository.addTask(task)
        }
    }
}