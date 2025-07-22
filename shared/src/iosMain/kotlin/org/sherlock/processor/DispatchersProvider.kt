package org.sherlock.processor

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

private class IosDispatchersProvider : DispatchersProvider {
    override fun io(): CoroutineDispatcher = Dispatchers.IO
    override fun ui(): CoroutineDispatcher = Dispatchers.Main
}

actual fun dispatchersProvider(): DispatchersProvider = IosDispatchersProvider()