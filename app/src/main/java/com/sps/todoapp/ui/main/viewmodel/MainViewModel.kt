package com.sps.todoapp.ui.main.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sps.todoapp.data.local.room.entity.Task
import com.sps.todoapp.repository.MainRepository
import com.sps.todoapp.ui.main.viewmodel.MainViewModel.State.AddTask
import com.sps.todoapp.ui.main.viewmodel.MainViewModel.State.Error
import com.sps.todoapp.ui.main.viewmodel.MainViewModel.State.Loading
import com.sps.todoapp.ui.main.viewmodel.MainViewModel.State.TaskScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    private val _screenState = MutableStateFlow<State>(Loading)
    val screenState: StateFlow<State> = _screenState
    private val _searchText = MutableStateFlow<String>("")
    val searchText: StateFlow<String> = _searchText
    private val _taskNameText = MutableStateFlow<String>("")
    val taskNameText: StateFlow<String> = _taskNameText
    private val _isSearching = MutableStateFlow<Boolean>(false)
    val isSearching: StateFlow<Boolean> = _isSearching
    private val _allTasks = MutableStateFlow<List<Task>>(emptyList())

    init {
        fetchTasks()
    }

    fun fetchTasks() {
        viewModelScope.launch(Dispatchers.IO) {
            _screenState.value = Loading

            repository.getAllTasks().collect { tasks ->

                if (tasks.isEmpty()) {
                    _allTasks.value = emptyList()
                    _screenState.value = Error
                }
                else {
                    _allTasks.value = tasks
                    _screenState.value = TaskScreen(tasks)
                }
            }
        }
    }

    fun addTask() {
        viewModelScope.launch(Dispatchers.Main ) {
            try {
                taskNameText.value.let { taskName ->
                    if (taskName.equals("Error", ignoreCase = true)) {
                        throw Exception("Failed to add TODO")
                    } else {
                        _screenState.value = Loading
                        delay(2000)
                        withContext(Dispatchers.IO) {
                            repository.addTask(Task(taskName = taskNameText.value))
                        }
                    }
                }
            } catch (exception: Exception) {
                //Log error here
                fetchTasks()
            }
        }
    }

    fun isValid() = taskNameText.value.isNotBlank()

    fun addTaskState() {
        _screenState.value = AddTask
    }

    fun onSearchTextChange(text: String) {
        _searchText.value = text
        if (text.isBlank()) {
            CoroutineScope(Dispatchers.Main).launch {
                _allTasks.map { tasks ->
                    TaskScreen(tasks)
                }.collect { tasks ->
                    _screenState.value = tasks
                }
            }
        } else {
            CoroutineScope(Dispatchers.Main).launch {
                _allTasks.map { tasks ->
                    val filteredTasks = tasks.filter { it.taskName.contains(text, ignoreCase = true) }
                    TaskScreen((filteredTasks))
                }.collect { newState ->
                    _screenState.value = newState
                }
            }
        }
    }

    fun onTaskNameChange(newName: String) {
        _taskNameText.value = newName
    }

    fun onToogleSearch() {
        _isSearching.value = !_isSearching.value
        if (!_isSearching.value) {
            onSearchTextChange("")
        }
    }

    sealed class State() {
        data object Loading : State()
        data object Empty : State()
        class TaskScreen(val tasks: List<Task>): State()
        data object AddTask : State()
        data object Error : State()
    }
}