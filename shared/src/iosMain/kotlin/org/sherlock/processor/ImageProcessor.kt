package org.sherlock.processor

import cocoapods.GoogleMLKit.MLKCommonTextRecognizerOptions
import cocoapods.GoogleMLKit.MLKTextRecognizer
import cocoapods.GoogleMLKit.MLKVisionImage
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.suspendCancellableCoroutine
import objcnames.protocols.MLKCompatibleImageProtocol
import platform.Foundation.NSLog
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@OptIn(ExperimentalForeignApi::class)
actual class ImageProcessor {
    private val recognizer = MLKTextRecognizer.textRecognizerWithOptions(MLKCommonTextRecognizerOptions())

    actual suspend fun processImage(image: Image): String? =
        try {
            val visionImage = image.toVisionImage()
            suspendCancellableCoroutine { continuation ->
                recognizer.processImage(visionImage) { text, error ->
                    if (error != null || text == null) {
                        continuation.resumeWithException(
                            IllegalStateException(
                                error?.localizedDescription ?: "No text recognized"
                            )
                        )
                    } else {
                        continuation.resume(text.text)
                    }
                }
            }
        } catch (e: Exception) {
            NSLog("Error processing image: ${e.stackTraceToString()}")
            null
        }

    @Suppress("UNCHECKED_CAST_TO_FORWARD_DECLARATION")
    private fun Image.toVisionImage(): MLKCompatibleImageProtocol {
        val visionImage = MLKVisionImage(uiImage)
        visionImage.orientation = uiImage.imageOrientation
        return visionImage as MLKCompatibleImageProtocol
    }
}