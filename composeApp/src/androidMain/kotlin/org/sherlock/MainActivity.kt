package org.sherlock

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickMultipleVisualMedia
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.sherlock.processor.Constants.LOG_TAG
import org.sherlock.processor.Image
import org.sherlock.processor.TextExtractor
import org.sherlock.ui.screens.MainScreen

class MainActivity : ComponentActivity() {
    private val selectImages = registerForActivityResult(
        contract = PickMultipleVisualMedia(),
        callback = { uris -> processImages(uris) }
    )
    private val scope = MainScope()
    private val textExtractor: TextExtractor
        get() = (application as SherlockApplication).textExtractor

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            MainScreen(
                onSelectImage = {
                    selectImages()
                },
                onSearchImage = {
                    // TODO: Finish connecting
                },
            )
        }
    }

    private fun selectImages() {
        selectImages.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
    }

    private fun processImages(uris: List<Uri>) {
        val images = uris.map { Image(uri = it) }

        scope.launch {
            textExtractor.processImages(images)
        }
    }

    private fun searchImage(text: String) {
        scope.launch {
            //TODO: Connect to ui
            Log.d(LOG_TAG, textExtractor.search(text).toString())
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        scope.cancel()
    }
}