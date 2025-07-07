package org.sherlock.sherlock

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(
    onSelectImage: () -> Unit,
    onProcessAllImages: () -> Unit,
    modifier: Modifier = Modifier,
) {
    MaterialTheme {
        Scaffold(
            modifier = modifier,
        ) { scaffoldPadding ->
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(scaffoldPadding)
            ) {
                Button(onClick = onSelectImage) {
                    Text("Select an image")
                }

                Button(onClick = onProcessAllImages) {
                    Text("Process all images")
                }
            }
        }
    }
}