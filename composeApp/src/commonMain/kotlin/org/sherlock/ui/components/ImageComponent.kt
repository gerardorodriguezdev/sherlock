package org.sherlock.ui.components

import androidx.compose.runtime.Composable

@Composable
expect fun ImageComponent(key: String, contentDescription: String? = null)