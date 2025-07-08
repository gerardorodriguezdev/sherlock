package org.sherlock.ui.components

import androidx.compose.runtime.Composable
import coil3.compose.AsyncImage

@Composable
actual fun ImageComponent(key: String, contentDescription: String?) {
    AsyncImage(model = key, contentDescription = contentDescription)
}