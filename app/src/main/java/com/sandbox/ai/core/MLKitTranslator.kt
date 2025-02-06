package com.sandbox.ai.core

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MLKitTranslator {
    private val options = TranslatorOptions.Builder()
        .setSourceLanguage(TranslateLanguage.ENGLISH)
        .setTargetLanguage(TranslateLanguage.KOREAN)
        .build()
    private val conditions = DownloadConditions.Builder()
        .requireWifi()
        .build()
    private var isModelReady = false
    private val englishKoreanTranslator = Translation.getClient(options).also {
        it.downloadModelIfNeeded(conditions)
            .addOnSuccessListener {
                Log.e("MLKitTranslator", "Model downloaded successfully")
                isModelReady = true
            }
            .addOnFailureListener {
                Log.e("MLKitTranslator", "Model download failed: $it")
                isModelReady = false
            }
    }

    fun translate(description: String, callback: (String) -> Unit) {
        if (isModelReady) {
            englishKoreanTranslator.translate(description)
                .addOnSuccessListener { callback(it) }
                .addOnFailureListener { exception ->
                    callback("Translation failed: $exception")
                }
        } else {
            callback("Model is not ready")
        }
    }

    fun close() {
        englishKoreanTranslator.close()
    }
}