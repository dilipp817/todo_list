package com.sps.todoapp.ui.main.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.sps.todoapp.R
import com.sps.todoapp.ui.main.viewmodels.HomeViewModel
import com.sps.todoapp.ui.main.viewmodels.HomeViewModel.State.TaskScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(modifier: Modifier, screenState: TaskScreen) {
    val viewModel = hiltViewModel<HomeViewModel>()
    val taskList = (screenState as TaskScreen).tasks
    val searchQuery by viewModel.searchText.collectAsState()
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        SearchBar(
            query = searchQuery,
            onQueryChange = viewModel::onSearchTextChange,
            onSearch = viewModel::onSearchTextChange,
            active = false,
            onActiveChange = { viewModel.onToggleSearch() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.dimen_16))
        ) {}
        ListContent(
            modifier = Modifier,
            tasksList = taskList
        )
    }
}