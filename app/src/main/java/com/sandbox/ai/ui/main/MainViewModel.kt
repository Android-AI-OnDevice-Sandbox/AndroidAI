package com.sandbox.ai.ui.main

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sandbox.ai.core.GemmaLLM
import com.sandbox.ai.core.MLKitTranslator
import com.sandbox.ai.data.api.ComputerVisionService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val computerVisionService = ComputerVisionService()

    private val _captions = MutableStateFlow<List<String>>(emptyList())
    val captions: StateFlow<List<String>> = _captions.asStateFlow()

    private val _translatedCaptions = MutableStateFlow<String?>(null)
    val translatedCaptions: StateFlow<String?> = _translatedCaptions.asStateFlow()

    private val translator = MLKitTranslator()

    fun getCaption(image: Uri, context: Context) {
        viewModelScope.launch {
            try {
                context.contentResolver.openInputStream(image)?.use { inputStream ->
                    val imageBytes = inputStream.readBytes()
                    val result = computerVisionService.analyzeLocalImage(imageBytes)
                    _captions.value = result.denseCaptionsResult.values.map { it.text }
                }
                llmThenTranslate(context)
            } catch (e: Exception) {
                Log.e("PickPhotoViewModel", "Error getting captions", e)
                _captions.value = emptyList()
            }
        }
    }

    private fun llmThenTranslate(context: Context) {
        viewModelScope.launch {
            try {
                val llmResult = GemmaLLM(context, captions.value.joinToString(" ")).getResult()
                val translated = translator.translate(llmResult)
                _translatedCaptions.tryEmit(translated)
            } catch (e: Exception) {
                Log.e("PickPhotoViewModel", "Error getting LLM result", e)
                _translatedCaptions.tryEmit(null)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        translator.close()
    }
}