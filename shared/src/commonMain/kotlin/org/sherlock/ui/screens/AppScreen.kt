package org.sherlock.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.sherlock.ui.components.ImageComponent
import org.sherlock.ui.screens.AppScreenConstants.IMAGE_COMPONENT_CONTENT_TYPE

@Composable
fun AppScreen(
    images: ImmutableList<String>,
    onSearchImage: (text: String) -> Unit,
    onSelectImagesClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    MaterialTheme {
        var dialogImage by remember { mutableStateOf<String?>(null) }

        NavigationSuiteScaffold(
            navigationSuiteItems = {
                item(
                    selected = true,
                    onClick = {},
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text(text = "Home", textAlign = TextAlign.Center) },
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
                SearchBar(
                    onSearchImage = onSearchImage,
                    onSelectImagesClicked = onSelectImagesClicked,
                )

                Content(
                    images = images,
                    onImageClick = { dialogImage = it },
                )
            }
        }

        dialogImage?.let {
            Dialog(
                onDismissRequest = { dialogImage = null },
                properties = DialogProperties(usePlatformDefaultWidth = false)
            ) {
                ZoomableImageComponent(key = it, modifier = Modifier.fillMaxWidth())
            }
        }
    }
}

@Composable
fun SearchBar(
    onSearchImage: (String) -> Unit,
    onSelectImagesClicked: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        val textFieldState = rememberTextFieldState()
        OutlinedTextField(
            state = textFieldState,
            trailingIcon = {
                IconButton(onClick = { onSearchImage(textFieldState.text.toString()) }) {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            lineLimits = TextFieldLineLimits.SingleLine,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            onKeyboardAction = KeyboardActionHandler {
                onSearchImage(textFieldState.text.toString())
            },
            placeholder = { Text(text = "Search...") },
        )

        OutlinedIconButton(onClick = { onSelectImagesClicked() }) {
            Icon(imageVector = Icons.Default.ImageSearch, contentDescription = "Select images")
        }
    }
}

@Composable
private fun Content(
    images: ImmutableList<String>,
    onImageClick: (String) -> Unit,
) {
    if (images.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "No matches found", textAlign = TextAlign.Center)
        }
    } else {
        val columns = if (images.size > 1) 2 else 1
        LazyVerticalGrid(
            columns = GridCells.Fixed(columns),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize(),
        ) {
            items(items = images, contentType = { IMAGE_COMPONENT_CONTENT_TYPE }, key = { it }) { image ->
                ImageComponent(
                    key = image,
                    modifier = Modifier.clickable { onImageClick(image) },
                )
            }
        }
    }

}

@Composable
private fun ZoomableImageComponent(key: String, modifier: Modifier) {
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    val state = rememberTransformableState { zoomChange, offsetChange, rotationChange ->
        scale *= zoomChange
        offset += offsetChange
    }

    ImageComponent(
        key = key,
        modifier = modifier
            .graphicsLayer(
                scaleX = scale,
                scaleY = scale,
                translationX = offset.x,
                translationY = offset.y
            )
            .transformable(state = state)
    )
}

private object AppScreenConstants {
    const val IMAGE_COMPONENT_CONTENT_TYPE = 1
}

@Preview
@Composable
private fun MainScreenPreview() {
    AppScreen(
        images = persistentListOf(),
        onSelectImagesClicked = {},
        onSearchImage = {},
    )
}