package org.sherlock

import android.app.Application
import org.sherlock.processor.*

class SherlockApplication : Application() {

    lateinit var textExtractor: TextExtractor<AndroidImage>
        private set

    override fun onCreate() {
        super.onCreate()

        textExtractor = TextExtractor(
            imageProcessor = AndroidImageProcessor(this),
            dispatchersProvider = AndroidDispatcherProvider(),
            tracer = AndroidTracer(),
        )
    }
}