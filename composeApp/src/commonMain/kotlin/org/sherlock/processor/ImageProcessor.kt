package org.sherlock.processor

interface ImageProcessor {
    suspend fun processImage(image: Image): String?
}