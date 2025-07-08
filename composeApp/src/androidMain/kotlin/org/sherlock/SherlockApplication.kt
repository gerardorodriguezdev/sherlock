package org.sherlock

import android.app.Application
import org.sherlock.processor.AndroidDispatcherProvider
import org.sherlock.processor.AndroidImageProcessor
import org.sherlock.processor.TextExtractor

class SherlockApplication : Application() {

    lateinit var textExtractor: TextExtractor
        private set

    override fun onCreate() {
        super.onCreate()

        textExtractor = TextExtractor(
            imageProcessor = AndroidImageProcessor(this),
            dispatchersProvider = AndroidDispatcherProvider(),
        )
    }
}