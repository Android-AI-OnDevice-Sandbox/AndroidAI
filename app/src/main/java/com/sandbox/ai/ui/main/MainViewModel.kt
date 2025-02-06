package com.sandbox.ai.ui.main

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import com.sandbox.ai.data.api.ComputerVisionService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val computerVisionService = ComputerVisionService()

    private val _captions = MutableStateFlow<List<String>>(emptyList())
    val captions: StateFlow<List<String>> = _captions.asStateFlow()

    private val _translatedCaptions = MutableStateFlow<List<String>>(emptyList())
    val translatedCaptions: StateFlow<List<String>> = _translatedCaptions.asStateFlow()

    private val translator by lazy {
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.ENGLISH)
            .setTargetLanguage(TranslateLanguage.KOREAN)
            .build()
        Translation.getClient(options)
    }

    init {
        downloadTranslationModel()
    }

    private fun downloadTranslationModel() {
        val conditions = DownloadConditions.Builder()
            .requireWifi()
            .build()
        translator.downloadModelIfNeeded(conditions)
    }

    fun getCaption(image: Uri, context: Context) {
        viewModelScope.launch {
            try {
                context.contentResolver.openInputStream(image)?.use { inputStream ->
                    val imageBytes = inputStream.readBytes()
                    val result = computerVisionService.analyzeLocalImage(imageBytes)
                    _captions.value = result.denseCaptionsResult.values.map { it.text }
                }
                translateCaptions()
            } catch (e: Exception) {
                Log.e("PickPhotoViewModel", "Error getting captions", e)
                _captions.value = emptyList()
            }
        }
    }

    private fun translateCaptions() {
        viewModelScope.launch {
            val translatedList = mutableListOf<String>()
            captions.value.forEach { caption ->
                try {
                    translator.translate(caption)
                        .addOnSuccessListener { translatedText ->
                            translatedList.add(translatedText)
                            if (translatedList.size == captions.value.size) {
                                _translatedCaptions.value = translatedList
                            }
                        }
                } catch (e: Exception) {
                    Log.e("PickPhotoViewModel", "Translation error", e)
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        translator.close()
    }
}