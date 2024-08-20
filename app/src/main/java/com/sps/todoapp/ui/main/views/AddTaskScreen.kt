package com.sps.todoapp.ui.main.views

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sps.todoapp.ui.main.viewmodels.HomeViewModel
import com.sps.todoapp.R

@Composable
fun AddTaskScreen(
    modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(dimensionResource(id = R.dimen.dimen_16))
    ) {
        val viewModel = hiltViewModel<HomeViewModel>()
        val context = LocalContext.current
        TaskDescriptionInput(modifier.weight(0.7f)) // Assuming there's a description input
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.dimen_16)))
        TextButton(
            modifier = modifier
                .weight(0.2f)
                .fillMaxWidth()
                .background(color = Color.Green)
                .align(Alignment.CenterHorizontally),
            onClick = {
                if (viewModel.isValid().not()) {
                    Toast.makeText(context, "Task name should not be blank", Toast.LENGTH_SHORT).show()
                }
                else {
                    viewModel.addTask()
                }
            }) {
            Text(text = stringResource(id = R.string.txt_save))
        }

    }
}

@Composable
fun TaskDescriptionInput(modifier: Modifier = Modifier) {
    var description by remember { mutableStateOf("") }
    val viewModel = hiltViewModel<HomeViewModel>()

    BasicTextField(
        value = description,
        onValueChange = {
            description = it
            viewModel.onTaskNameChange(it)
                        },
        textStyle = TextStyle(color = Color.Black, fontSize = 18.sp),
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.Gray)
            .padding(dimensionResource(id = R.dimen.dimen_16))
    )
}