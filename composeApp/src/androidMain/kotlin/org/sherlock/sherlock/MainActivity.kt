package org.sherlock.sherlock

import android.Manifest.permission.READ_MEDIA_IMAGES
import android.content.ContentResolver
import android.content.ContentUris
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.*
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.sherlock.sherlock.ui.screens.MainScreen
import java.io.IOException

class MainActivity : ComponentActivity() {
    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    private val pickMedia = registerForActivityResult(PickMultipleVisualMedia()) { uris ->
        uris.forEach { uri -> processImage(uri) }
    }

    private val requestPermissions = registerForActivityResult(RequestMultiplePermissions()) { results ->
        if (results.all { it.value }) {
            CoroutineScope(Dispatchers.IO).launch {
                val uris = getUris(contentResolver)

                uris.forEach { uri ->
                    processImage(uri)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            MainScreen(
                onSelectImage = {
                    selectImage()
                },
                onProcessAllImages = {
                    processAllImages()
                }
            )
        }
    }

    private fun selectImage() {
        pickMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
    }

    private fun processAllImages() {
        requestPermissions.launch(arrayOf(READ_MEDIA_IMAGES))
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

    suspend fun getUris(contentResolver: ContentResolver): List<Uri> =
        withContext(Dispatchers.IO) {
            val projection = arrayOf(
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.MIME_TYPE,
            )

            val collectionUri = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)

            val uris = mutableListOf<Uri>()

            contentResolver.query(
                collectionUri,
                projection,
                null,
                null,
                "${MediaStore.Images.Media.DATE_ADDED} DESC"
            )?.use { cursor ->
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)

                while (cursor.moveToNext()) {
                    val uri = ContentUris.withAppendedId(collectionUri, cursor.getLong(idColumn))

                    uris.add(uri)
                }
            }

            return@withContext uris
        }
}