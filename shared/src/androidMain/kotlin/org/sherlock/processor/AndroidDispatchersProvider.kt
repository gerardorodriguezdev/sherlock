package org.sherlock.processor

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class AndroidDispatcherProvider : DispatchersProvider {
    override fun io(): CoroutineDispatcher = Dispatchers.IO
    override fun ui(): CoroutineDispatcher = Dispatchers.Main
}