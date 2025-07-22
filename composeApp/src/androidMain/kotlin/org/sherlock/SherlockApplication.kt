package org.sherlock

import android.app.Application
import org.sherlock.processor.ImageProcessor
import org.sherlock.processor.TextExtractor

class SherlockApplication : Application() {

    lateinit var textExtractor: TextExtractor
        private set

    override fun onCreate() {
        super.onCreate()

        textExtractor = TextExtractor(
            imageProcessor = ImageProcessor(this),
        )
    }
}