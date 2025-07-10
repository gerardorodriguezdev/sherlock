package org.benchmark

import androidx.benchmark.junit4.BenchmarkRule
import androidx.benchmark.junit4.measureRepeated
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TextExtractorBenchmark {

    @get:Rule
    val benchmarkRule = BenchmarkRule()

    @Test
    fun processImages() {
        benchmarkRule.measureRepeated {

        }
    }

    @Test
    fun tokenization() {
        benchmarkRule.measureRepeated {

        }
    }

    @Test
    fun textSearch() {
        benchmarkRule.measureRepeated {

        }
    }
}