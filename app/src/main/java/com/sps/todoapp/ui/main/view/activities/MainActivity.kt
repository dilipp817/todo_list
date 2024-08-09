package com.sps.todoapp.ui.main.view.activities

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import com.sps.todoapp.R
import com.sps.todoapp.ui.main.view.AddTaskScreen
import com.sps.todoapp.ui.main.view.EmptyContent
import com.sps.todoapp.ui.main.view.ListContent
import com.sps.todoapp.ui.main.view.LoadingScreen
import com.sps.todoapp.ui.main.viewmodel.MainViewModel
import com.sps.todoapp.ui.main.viewmodel.MainViewModel.State.AddTask
import com.sps.todoapp.ui.main.viewmodel.MainViewModel.State.Empty
import com.sps.todoapp.ui.main.viewmodel.MainViewModel.State.Error
import com.sps.todoapp.ui.main.viewmodel.MainViewModel.State.Loading
import com.sps.todoapp.ui.main.viewmodel.MainViewModel.State.TaskScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    val viewModel: MainViewModel by viewModels()
     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TopAppBarApp()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarApp(modifier: Modifier = Modifier) {
    val viewModel = hiltViewModel<MainViewModel>()
    val screenState by viewModel.screenState.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = modifier,
                title = {
                    Text(
                        text = stringResource(id = R.string.AppBar),
                    )
                },
                colors = topAppBarColors(
                    containerColor = colorResource(id = R.color.green),
                ),
                navigationIcon = { },
                actions = {}
            )
        },
        content = { paddingValues ->

            when (screenState) {
                is Loading -> LoadingScreen(modifier.padding(paddingValues))
                is AddTask -> AddTaskScreen(modifier.padding(paddingValues))
                is Empty -> EmptyContent(modifier.padding(paddingValues))
                is Error -> EmptyContent(modifier.padding(paddingValues))
                is TaskScreen -> {
                    val taskList = (screenState as TaskScreen).tasks
                    val searchQuery by viewModel.searchText.collectAsState()
                    Column(
                        modifier = modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    ) {
                        SearchBar(
                            query = searchQuery,
                            onQueryChange = viewModel::onSearchTextChange,
                            onSearch = viewModel::onSearchTextChange,
                            active = false,
                            onActiveChange = { viewModel.onToogleSearch() },
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
            }
        },
        floatingActionButton = {
            if (screenState is Empty || screenState is Error || screenState is TaskScreen) {
                FloatingActionButton(
                    onClick = { viewModel.addTaskState() },
                    containerColor = colorResource(id = R.color.green),
                    contentColor = colorResource(id = R.color.white)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                }
            }
        }
    )
}
