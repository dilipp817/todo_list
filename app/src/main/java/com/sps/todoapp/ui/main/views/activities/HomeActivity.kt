package com.sps.todoapp.ui.main.views.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.sps.todoapp.R
import com.sps.todoapp.ui.main.views.AddTaskScreen
import com.sps.todoapp.ui.main.views.EmptyContent
import com.sps.todoapp.ui.main.views.LoadingScreen
import com.sps.todoapp.ui.main.views.TaskListScreen
import com.sps.todoapp.ui.main.viewmodels.HomeViewModel
import com.sps.todoapp.ui.main.viewmodels.HomeViewModel.State.AddTask
import com.sps.todoapp.ui.main.viewmodels.HomeViewModel.State.Empty
import com.sps.todoapp.ui.main.viewmodels.HomeViewModel.State.Error
import com.sps.todoapp.ui.main.viewmodels.HomeViewModel.State.Loading
import com.sps.todoapp.ui.main.viewmodels.HomeViewModel.State.TaskScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
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
    val viewModel = hiltViewModel<HomeViewModel>()
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
                is TaskScreen -> TaskListScreen(modifier.padding(paddingValues),
                    screenState as TaskScreen
                )
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
