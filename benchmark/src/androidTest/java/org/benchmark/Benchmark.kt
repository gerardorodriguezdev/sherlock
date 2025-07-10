package org.benchmark

import android.util.Log
import androidx.benchmark.junit4.BenchmarkRule
import androidx.benchmark.junit4.measureRepeated
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

//TODO: Time to process image
//TODO: Time to search texts
//TODO: Time to tokenize
@RunWith(AndroidJUnit4::class)
class Benchmark {

    @get:Rule
    val benchmarkRule = BenchmarkRule()

    @Test
    fun benchmark() {
        benchmarkRule.measureRepeated {
            Log.d("LogBenchmark", "the cost of writing this log method will be measured")
        }
    }
}