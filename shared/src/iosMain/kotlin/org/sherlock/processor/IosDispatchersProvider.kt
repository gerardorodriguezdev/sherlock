package org.sherlock.processor

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

class IosDispatchersProvider : DispatchersProvider {
    override fun io(): CoroutineDispatcher = Dispatchers.IO
    override fun ui(): CoroutineDispatcher = Dispatchers.Main
}