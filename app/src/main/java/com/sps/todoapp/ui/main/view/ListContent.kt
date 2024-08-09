package com.sps.todoapp.ui.main.view

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.sps.todoapp.R
import com.sps.todoapp.data.local.room.entity.Task

@Composable
fun ListContent(
    modifier: Modifier = Modifier,
    tasksList: List<Task>
) {

    LazyColumn {
        items(
            items = tasksList,
            key = { task -> task.id }
        ) { task ->
            TaskItem(
                todoTask = task
            )
        }
    }
}

@Composable
fun TaskItem(modifier: Modifier = Modifier, todoTask: Task) {
    Surface(
        modifier = modifier
            .fillMaxWidth(),
        shape = RectangleShape,
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.dimen_20)),
            text = todoTask.taskName,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}