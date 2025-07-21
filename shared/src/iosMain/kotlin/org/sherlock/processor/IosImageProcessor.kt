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
import cocoapods.GoogleMLKit.MLKCompatibleImageProtocol as GoogleMLKCompatibleImageProtocol

@OptIn(ExperimentalForeignApi::class)
class IosImageProcessor : ImageProcessor<IosImage> {
    private val recognizer = MLKTextRecognizer.textRecognizerWithOptions(MLKCommonTextRecognizerOptions())

    override suspend fun processImage(image: IosImage): String? =
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

    private fun IosImage.toVisionImage(): MLKCompatibleImageProtocol {
        val visionImage = MLKVisionImage(uiImage)
        visionImage.orientation = uiImage.imageOrientation
        return (visionImage as GoogleMLKCompatibleImageProtocol) as MLKCompatibleImageProtocol
    }
}