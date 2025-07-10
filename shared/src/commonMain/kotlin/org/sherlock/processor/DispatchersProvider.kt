package org.sherlock.processor

import kotlinx.coroutines.CoroutineDispatcher

interface DispatchersProvider {
    fun io(): CoroutineDispatcher
    fun ui(): CoroutineDispatcher
}