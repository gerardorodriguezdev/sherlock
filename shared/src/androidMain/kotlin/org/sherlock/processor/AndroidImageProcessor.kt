package org.sherlock.processor

import android.content.Context
import android.util.Log
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class AndroidImageProcessor(private val applicationContext: Context) : ImageProcessor<AndroidImage> {

    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    override suspend fun processImage(image: AndroidImage): String? =
        try {
            val inputImage = InputImage.fromFilePath(applicationContext, image.uri)
            suspendCancellableCoroutine { continuation ->
                recognizer
                    .process(inputImage)
                    .addOnSuccessListener { text ->
                        continuation.resume(text.text)
                    }
                    .addOnFailureListener { e ->
                        continuation.resumeWithException(e)
                    }
            }
        } catch (e: IOException) {
            Log.d(LOG_TAG, "Error processing image", e)
            null
        }

    private companion object {
        const val LOG_TAG = "Sherlock"
    }
}