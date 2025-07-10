package org.sherlock

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickMultipleVisualMedia
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.activity.viewModels
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import org.sherlock.presentation.MainViewModel
import org.sherlock.processor.Image
import org.sherlock.ui.screens.AppScreen

class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels {
        viewModelFactory {
            initializer {
                MainViewModel(textExtractor = (application as SherlockApplication).textExtractor)
            }
        }
    }
    private val selectImages = registerForActivityResult(
        contract = PickMultipleVisualMedia(),
        callback = { uris ->
            val images = uris.map { Image(uri = it) }
            mainViewModel.processImages(images)
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            AppScreen(
                images = mainViewModel.images.value,
                onSearchImage = { mainViewModel.searchImage(it) },
                onSelectImagesClicked = { selectImages() },
            )
        }
    }

    private fun selectImages() {
        selectImages.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
    }
}