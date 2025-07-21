package org.sherlock.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import coil3.compose.AsyncImage

@Composable
fun ImageComponent(key: String, contentDescription: String? = null, modifier: Modifier) {
    AsyncImage(model = key, contentDescription = contentDescription, modifier = modifier)
}