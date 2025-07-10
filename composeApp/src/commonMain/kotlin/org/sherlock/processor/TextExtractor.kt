package org.sherlock.processor

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import kotlin.coroutines.coroutineContext

class TextExtractor(
    private val imageProcessor: ImageProcessor,
    private val dispatchersProvider: DispatchersProvider,
) {
    private val entries = mutableMapOf<String, Set<String>>()

    suspend fun processImages(images: List<Image>) {
        entries.clear()

        val parentContext = coroutineContext
        withContext(dispatchersProvider.io()) {
            val entries = images
                .map { image ->
                    async { image.processImage() }
                }
                .awaitAll()
                .filterNotNull()

            withContext(parentContext) {
                this@TextExtractor.entries.putAll(entries)
            }
        }
    }

    private suspend fun Image.processImage(): Pair<String, Set<String>>? {
        val text = imageProcessor.processImage(this)
        val tokens = text?.toTokens()

        return tokens?.let {
            Pair(key, tokens)
        }
    }

    private fun String.toTokens(): Set<String> =
        regex.findAll(this).map { matchResult -> matchResult.value.lowercase() }.toSet()

    suspend fun search(text: String): List<String> {
        val parentContext = coroutineContext
        return withContext(dispatchersProvider.io()) {
            val searchTokens = text.toTokens()

            val keys = entries
                .map { (key, value) ->
                    async {
                        val entryContainsToken = value.containsAny(searchTokens)
                        if (entryContainsToken) key else null
                    }
                }
                .awaitAll()
                .filterNotNull()

            withContext(parentContext) {
                keys
            }
        }
    }

    private fun Set<String>.containsAny(strings: Set<String>): Boolean = any { strings.contains(it) }

    private companion object {
        val regex = Regex("\\b\\w+\\b")
    }
}