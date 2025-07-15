package org.benchmark

import android.net.Uri
import androidx.benchmark.ExperimentalBenchmarkConfigApi
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
import org.sherlock.processor.*
import java.io.File
import java.io.FileOutputStream

@RunWith(AndroidJUnit4::class)
class TextExtractorBenchmark {

    @OptIn(ExperimentalBenchmarkConfigApi::class)
    @get:Rule
    val benchmarkRule = BenchmarkRule()

    private lateinit var textExtractor: TextExtractor<AndroidImage>
    private val entries = mutableMapOf<String, HashSet<String>>()

    @Before
    fun setUp() {
        textExtractor = TextExtractor(
            imageProcessor = AndroidImageProcessor(InstrumentationRegistry.getInstrumentation().targetContext),
            dispatchersProvider = AndroidDispatcherProvider(),
            tracer = EmptyTracer(),
            entries = entries,
        )
    }

    @Test
    fun processImages() {
        benchmarkRule.measureRepeated {
            val images = runWithMeasurementDisabled { images() }
            runBlocking {
                textExtractor.processImages(images)
            }
            assertEquals(5, entries.size)
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
                val results = textExtractor.search("One")
                assertEquals(1_000, results.size)
            }
        }
    }

    private fun images(): List<AndroidImage> = buildList {
        repeat(5) { index ->
            val imageName = imageName(index)
            val inputStream = javaClass.classLoader?.getResourceAsStream(imageName)
                ?: throw IllegalStateException("$imageName resource not found")
            val cacheFile = File(InstrumentationRegistry.getInstrumentation().targetContext.cacheDir, imageName)
            inputStream.use { input ->
                FileOutputStream(cacheFile).use { output ->
                    input.copyTo(output)
                }
            }
            add(AndroidImage(uri = Uri.fromFile(cacheFile)))
        }
    }

    private fun sampleText(): String {
        val inputStream = javaClass.classLoader?.getResourceAsStream(SAMPLE_FILE_NAME)
            ?: throw IllegalStateException("$SAMPLE_FILE_NAME resource not found")
        return inputStream.bufferedReader().use { resource -> resource.readText() }
    }

    private fun addEntries() {
        repeat(1_000) { index ->
            entries.put(index.toString(), tokens)
        }
    }

    companion object {
        const val SAMPLE_FILE_NAME = "SampleText.txt"
        val tokens = hashSetOf("one", "two", "three", "four", "five")
        fun imageName(index: Int): String = "img${index}.jpg"
    }
}