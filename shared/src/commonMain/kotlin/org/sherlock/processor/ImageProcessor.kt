package org.sherlock.processor

expect class ImageProcessor {
    suspend fun processImage(image: Image): String?
}