package org.sherlock.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ImageSearch
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.sherlock.ui.components.ImageComponent
import org.sherlock.ui.screens.AppScreenConstants.IMAGE_COMPONENT_CONTENT_TYPE

@Composable
fun MainScreen(
    images: ImmutableList<String>,
    onSearchImage: (text: CharSequence) -> Unit,
    onSelectImages: () -> Unit,
    modifier: Modifier = Modifier,
) {
    MaterialTheme {
        NavigationSuiteScaffold(
            navigationSuiteItems = {
                item(
                    selected = true,
                    onClick = {},
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text(text = "Home") },
                )
            },
            modifier = modifier,
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 16.dp)
                    .safeDrawingPadding()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    val state = rememberTextFieldState()
                    OutlinedTextField(
                        state = state,
                        trailingIcon = {
                            IconButton(onClick = { onSearchImage(state.text) }) {
                                Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        lineLimits = TextFieldLineLimits.SingleLine,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        onKeyboardAction = KeyboardActionHandler {
                            onSearchImage(state.text)
                        }
                    )

                    IconButton(onClick = { onSelectImages() }) {
                        Icon(imageVector = Icons.Default.ImageSearch, contentDescription = "Select images")
                    }
                }

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize(),
                ) {
                    items(items = images, contentType = { IMAGE_COMPONENT_CONTENT_TYPE }) { item ->
                        ImageComponent(item)
                    }
                }
            }
        }
    }
}

private object AppScreenConstants {
    const val IMAGE_COMPONENT_CONTENT_TYPE = 1
}

@Preview
@Composable
private fun MainScreenPreview() {
    MainScreen(
        images = persistentListOf(),
        onSelectImages = {},
        onSearchImage = {},
    )
}