package com.sandbox.ai.core

import android.util.Log
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.TranslateRemoteModel
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions

class MLKitTranslator {
    private val options = TranslatorOptions.Builder()
        .setSourceLanguage(TranslateLanguage.ENGLISH)
        .setTargetLanguage(TranslateLanguage.KOREAN)
        .build()
    private val conditions = DownloadConditions.Builder()
        // Wi-Fi is only accepted for downloading the model
        .requireWifi()
        .build()
    private val modelManager = RemoteModelManager.getInstance()
    private var isModelReady = false
    private val englishKoreanTranslator = Translation.getClient(options)

    fun downloadModel(){
        englishKoreanTranslator.downloadModelIfNeeded(conditions)
            .addOnSuccessListener {
                Log.e("MLKitTranslator", "Model downloaded successfully")
                isModelReady = true
            }
            .addOnFailureListener {
                Log.e("MLKitTranslator", "Model download failed: $it")
                isModelReady = false
            }
    }

    fun deleteModel(){
        val koreanModel = TranslateRemoteModel.Builder(TranslateLanguage.KOREAN).build()
        modelManager.deleteDownloadedModel(koreanModel)
            .addOnSuccessListener {
                Log.e("MLKitTranslator", "Model deleted")
            }
            .addOnFailureListener {
                Log.e("MLKitTranslator", "Model deletion failed")
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