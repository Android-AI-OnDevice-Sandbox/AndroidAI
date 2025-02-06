package com.sandbox.ai.core

import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions

class MLKitTranslator {
    private val options = TranslatorOptions.Builder()
        .setSourceLanguage(TranslateLanguage.ENGLISH)
        .setTargetLanguage(TranslateLanguage.KOREAN)
        .build()
    private val englishKoreanTranslator = Translation.getClient(options)

    private val conditions = DownloadConditions.Builder()
        .requireWifi()
        .build()
    private var isModelReady = false

    init {
        modelSetup()
    }

    private fun modelSetup(): Boolean {
        englishKoreanTranslator.downloadModelIfNeeded(conditions)
            .addOnSuccessListener {
                isModelReady = true
            }
            .addOnFailureListener {
                isModelReady = false
            }
        return isModelReady
    }

    fun translate(descriptions: List<String>): String {
        var translatedText = ""
        if (isModelReady) {
            descriptions.forEach {
                englishKoreanTranslator.translate(it)
                    .addOnSuccessListener { translatedText += it }
                    .addOnFailureListener { exception ->

                    }
            }
        } else {
            translatedText = "Model is not ready"
        }
        return translatedText
    }
}