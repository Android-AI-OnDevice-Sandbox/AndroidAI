package com.sandbox.ai.core

import android.util.Log
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions

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

    fun translate(description: String): String {
        var translatedText = ""
        if (isModelReady) {
            englishKoreanTranslator.translate(description)
                .addOnSuccessListener { translatedText += it }
                .addOnFailureListener { exception ->
                    translatedText = "Translation failed: $exception"
                }
        } else {
            translatedText = "Model is not ready"
        }
        return translatedText
    }

    fun close() {
        englishKoreanTranslator.close()
    }
}