package org.benchmark

import androidx.benchmark.ExperimentalBenchmarkConfigApi
import androidx.benchmark.MicrobenchmarkConfig
import androidx.benchmark.junit4.BenchmarkRule
import androidx.benchmark.junit4.measureRepeated
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.sherlock.processor.AndroidDispatcherProvider
import org.sherlock.processor.AndroidImageProcessor
import org.sherlock.processor.TextExtractor

@RunWith(AndroidJUnit4::class)
class TextExtractorBenchmark {

    @OptIn(ExperimentalBenchmarkConfigApi::class)
    @get:Rule
    val benchmarkRule = BenchmarkRule(config = MicrobenchmarkConfig())

    private lateinit var textExtractor: TextExtractor
    private val entries = mutableMapOf<String, HashSet<String>>()

    @Before
    fun setUp() {
        textExtractor = TextExtractor(
            imageProcessor = AndroidImageProcessor(InstrumentationRegistry.getInstrumentation().targetContext),
            dispatchersProvider = AndroidDispatcherProvider(),
            entries = entries,
        )
    }

    @Test
    fun processImages() {
        benchmarkRule.measureRepeated {

        }
    }

    @Test
    fun tokenization() {
        benchmarkRule.measureRepeated {
            val sampleText = runWithMeasurementDisabled { sampleText() }
            val tokens = with(textExtractor) { sampleText.toTokens() }
            assertEquals(560, tokens.size)
        }
    }

    @Test
    fun textSearch() {
        benchmarkRule.measureRepeated {
            runWithMeasurementDisabled { addEntries() }

            runBlocking {
                textExtractor.search("One")
            }
        }
    }

    private fun sampleText(): String {
        val inputStream = javaClass.classLoader?.getResourceAsStream("SampleText.txt")
            ?: throw IllegalStateException("SampleText.txt resource not found")
        return inputStream.bufferedReader().use { resource -> resource.readText() }
    }

    private fun addEntries() {
        repeat(1_000) { index ->
            entries.put(index.toString(), hashSetOf("One", "Two", "Three", "Four", "Five"))
        }
    }
}