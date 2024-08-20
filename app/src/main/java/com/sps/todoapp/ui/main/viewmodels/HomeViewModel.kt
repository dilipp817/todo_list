package com.sps.todoapp.ui.main.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sps.data.local.room.entity.Task
import com.sps.data.repository.MainRepository
import com.sps.todoapp.ui.main.viewmodels.HomeViewModel.State.AddTask
import com.sps.todoapp.ui.main.viewmodels.HomeViewModel.State.Error
import com.sps.todoapp.ui.main.viewmodels.HomeViewModel.State.Loading
import com.sps.todoapp.ui.main.viewmodels.HomeViewModel.State.TaskScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    private val _screenState = MutableStateFlow<State>(Loading)
    val screenState: StateFlow<State> = _screenState
    private val _searchText = MutableStateFlow<String>("")
    val searchText: StateFlow<String> = _searchText
    private val _taskNameText = MutableStateFlow<String>("")
    private val taskNameText: StateFlow<String> = _taskNameText
    private val _isSearching = MutableStateFlow<Boolean>(false)
    private val isSearching: StateFlow<Boolean> = _isSearching
    private val _allTasks = MutableStateFlow<List<Task>>(emptyList())

    init {
        fetchTasks()
    }

    private fun fetchTasks() {
        viewModelScope.launch(Dispatchers.IO) {
            _screenState.value = Loading

            repository.getAllTasks().collect { tasks ->

                if (tasks.isEmpty()) {
                    _allTasks.value = emptyList()
                    _screenState.value = Error
                } else {
                    _allTasks.value = tasks
                    _screenState.value = TaskScreen(tasks)
                }
            }
        }
    }

    fun addTask() {
        viewModelScope.launch(Dispatchers.Main) {
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
                }
                    .flowOn(Dispatchers.Default)
                    .collect { tasks ->
                        _screenState.value = tasks
                    }
            }
        } else {
            CoroutineScope(Dispatchers.Main).launch {
                flowOf(text)
                    .debounce(200)
                    .distinctUntilChanged()
                    .flatMapLatest { text ->
                        _allTasks.map { tasks ->
                            val filteredTasks =
                                tasks.filter { it.taskName.contains(text, ignoreCase = true) }
                            TaskScreen((filteredTasks))
                        }
                    }
                    .flowOn(Dispatchers.Default)
                    .collect { newState ->
                        _screenState.value = newState
                    }
            }
        }
    }

    fun onTaskNameChange(newName: String) {
        _taskNameText.value = newName
    }

    fun onToggleSearch() {
        _isSearching.value = !_isSearching.value
        if (!isSearching.value) {
            onSearchTextChange("")
        }
    }

    sealed class State {
        data object Loading : State()
        data object Empty : State()
        class TaskScreen(val tasks: List<Task>) : State()
        data object AddTask : State()
        data object Error : State()
    }
}