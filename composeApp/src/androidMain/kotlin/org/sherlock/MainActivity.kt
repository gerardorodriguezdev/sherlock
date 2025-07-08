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
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import org.sherlock.ui.screens.MainScreen
import java.io.IOException

class MainActivity : ComponentActivity() {
    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    private val pickMedia = registerForActivityResult(PickMultipleVisualMedia()) { uris ->
        uris.forEach { uri -> processImage(uri) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            MainScreen(
                onSelectImage = {
                    selectImage()
                },
            )
        }
    }

    private fun selectImage() {
        pickMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
    }

    private fun processImage(uri: Uri) {
        try {
            val image = InputImage.fromFilePath(this, uri)
            recognizer.process(image)
                .addOnSuccessListener { visionText ->
                    Log.d("PhotoPicker", "Result for uri: $uri | Result: ${visionText.text}")
                }
                .addOnFailureListener { error ->
                    Log.d("PhotoPicker", "Error with uri: $uri | Exception: ${error.printStackTrace()}")
                }
        } catch (error: IOException) {
            Log.d("PhotoPicker", "Error with uri: $uri | Exception: ${error.printStackTrace()}")
        }
    }
}