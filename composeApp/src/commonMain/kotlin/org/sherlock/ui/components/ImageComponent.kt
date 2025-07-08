package org.sherlock.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun ImageComponent(key: String, contentDescription: String? = null, modifier: Modifier = Modifier)