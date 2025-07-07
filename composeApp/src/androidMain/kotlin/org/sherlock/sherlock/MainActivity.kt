package org.sherlock.sherlock

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.io.IOException

class MainActivity : ComponentActivity() {
    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia()) { uris ->
        uris.forEach { uri ->
            val image: InputImage
            try {
                image = InputImage.fromFilePath(this, uri)
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

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            App(
                onSelectImage = {
                    selectImage()
                }
            )
        }
    }

    private fun selectImage() {
        pickMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App(onSelectImage = {})
}