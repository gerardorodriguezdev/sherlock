package org.sherlock

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickMultipleVisualMedia
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.runtime.mutableStateOf
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.sherlock.processor.Image
import org.sherlock.processor.TextExtractor
import org.sherlock.ui.screens.MainScreen

class MainActivity : ComponentActivity() {
    private val selectImages = registerForActivityResult(
        contract = PickMultipleVisualMedia(),
        callback = { uris -> processImages(uris) }
    )
    private val scope = MainScope()
    private var currentJob: Job? = null
    private val textExtractor: TextExtractor
        get() = (application as SherlockApplication).textExtractor
    private val images = mutableStateOf(persistentListOf<String>())

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            MainScreen(
                images = images.value,
                onSearchImage = { searchImage(it.toString()) },
                onSelectImages = { selectImages() },
            )
        }
    }

    private fun selectImages() {
        selectImages.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
    }

    private fun processImages(uris: List<Uri>) {
        currentJob?.cancel()

        currentJob = scope.launch {
            val images = uris.map { Image(uri = it) }

            textExtractor.processImages(images)
        }
    }

    private fun searchImage(text: String) {
        currentJob?.cancel()

        currentJob = scope.launch {
            images.value = textExtractor.search(text).toPersistentList()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
}