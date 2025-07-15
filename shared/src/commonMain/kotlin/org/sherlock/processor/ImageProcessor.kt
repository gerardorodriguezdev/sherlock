package org.sherlock.processor

interface ImageProcessor<T: Image> {
    suspend fun processImage(image: T): String?
}