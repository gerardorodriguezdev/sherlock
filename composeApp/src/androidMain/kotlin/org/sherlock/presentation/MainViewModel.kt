package org.sherlock.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.sherlock.processor.AndroidImage
import org.sherlock.processor.TextExtractor

class MainViewModel(private val textExtractor: TextExtractor<AndroidImage>) : ViewModel() {
    private var currentJob: Job? = null

    val images = mutableStateOf(persistentListOf<String>())

    fun processImages(images: List<AndroidImage>) {
        currentJob?.cancel()

        currentJob = viewModelScope.launch {
            textExtractor.processImages(images)
        }
    }

    fun searchImage(text: String) {
        currentJob?.cancel()

        currentJob = viewModelScope.launch {
            images.value = textExtractor.search(text).toPersistentList()
        }
    }

    override fun onCleared() {
        super.onCleared()
        currentJob?.cancel()
    }
}