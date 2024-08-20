package com.sps.todoapp.ui.main.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.sps.todoapp.R

@Composable
fun EmptyContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier =  Modifier
                .fillMaxWidth(0.8f),
            text = stringResource(id = R.string.empty_page_text),
            fontSize = 20.sp
        )
    }
}



@Composable
@Preview
private fun EmptyContentPreview() {
    EmptyContent()
}