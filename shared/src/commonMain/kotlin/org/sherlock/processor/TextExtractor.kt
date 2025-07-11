package org.sherlock.processor

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import kotlin.coroutines.coroutineContext

class TextExtractor(
    private val imageProcessor: ImageProcessor,
    private val dispatchersProvider: DispatchersProvider,
    private val tracer: Tracer,
    private val entries: MutableMap<String, HashSet<String>> = mutableMapOf()
) {
    suspend fun processImages(images: List<Image>) {
        tracer.startTrace(PROCESS_IMAGES_TRACE_NAME)
        entries.clear()

        val parentContext = coroutineContext
        withContext(dispatchersProvider.io() + processImagesCoroutineName) {
            val entries = images
                .map { image ->
                    async { image.processImage() }
                }
                .awaitAll()
                .filterNotNull()

            withContext(parentContext) {
                this@TextExtractor.entries.putAll(entries)
                tracer.stopTrace()
            }
        }
    }

    private suspend fun Image.processImage(): Pair<String, HashSet<String>>? {
        val text = imageProcessor.processImage(this)
        val tokens = text?.toTokens()

        return tokens?.let {
            Pair(key, tokens)
        }
    }

    fun String.toTokens(): HashSet<String> =
        regex.findAll(this).map { matchResult -> matchResult.value.lowercase() }.toHashSet()

    suspend fun search(text: String): List<String> {
        tracer.startTrace(SEARCH_TOKENS_TRACE_NAME)
        val parentContext = coroutineContext
        return withContext(dispatchersProvider.io() + searchTokensCoroutineName) {
            val searchTokens = text.toTokens()

            val keys = entries
                .map { (key, tokens) ->
                    async {
                        val entryContainsToken = tokens.containsAny(searchTokens)
                        if (entryContainsToken) key else null
                    }
                }
                .awaitAll()
                .filterNotNull()

            withContext(parentContext) {
                tracer.stopTrace()
                keys
            }
        }
    }

    private fun HashSet<String>.containsAny(searchTokens: HashSet<String>): Boolean =
        any { token -> searchTokens.contains(token) }

    private companion object {
        val regex = Regex("\\b\\w+\\b")
        val processImagesCoroutineName = CoroutineName("ProcessImages")
        val searchTokensCoroutineName = CoroutineName("SearchTokens")
        const val PROCESS_IMAGES_TRACE_NAME = "processImages"
        const val SEARCH_TOKENS_TRACE_NAME = "searchTokens"
    }
}