package com.sps.todoapp.ui.main.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.sps.todoapp.data.local.room.entity.Task
import com.sps.todoapp.ui.main.viewmodel.MainViewModel
import com.sps.todoapp.ui.theme.ToDoAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContent {
            ToDoAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding),
                        viewModel
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier, viewModel: MainViewModel) {
//    val mainViewModel = hiltViewModel<MainViewModel>()
    val context = LocalContext.current
    Text(
        text = "Hello $name!",
        modifier = modifier
            .clickable {
                viewModel.addTask(Task(task = "task 1"))
                Toast.makeText( context, "item clicked", Toast.LENGTH_SHORT).show()
            }
    )
}

//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    ToDoAppTheme {
//        Greeting("Android")
//    }
//}