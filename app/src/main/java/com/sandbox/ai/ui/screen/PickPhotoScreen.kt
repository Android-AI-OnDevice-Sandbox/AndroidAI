package com.sandbox.ai.ui.screen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions

@Composable
fun PickPhotoScreen(
    modifier: Modifier = Modifier,
    viewModel: PickPhotoViewModel
) {
    var image: Uri? by rememberSaveable { mutableStateOf(null) }
    val context = LocalContext.current

    val descriptions by viewModel.captions.collectAsState()
    var isModelReady by rememberSaveable { mutableStateOf(false) }
    var translatedText by rememberSaveable { mutableStateOf("") }
    val translatedDescriptions by viewModel.translatedCaptions.collectAsState()

    val pickMedia = rememberLauncherForActivityResult(PickVisualMedia()) { uri ->
        if (uri != null) {
            image = uri
        } else {
        }
    }

    val options = TranslatorOptions.Builder()
        .setSourceLanguage(TranslateLanguage.ENGLISH)
        .setTargetLanguage(TranslateLanguage.KOREAN)
        .build()
    val englishKoreanTranslator = Translation.getClient(options)

    val conditions = DownloadConditions.Builder()
        .requireWifi()
        .build()
    englishKoreanTranslator.downloadModelIfNeeded(conditions)
        .addOnSuccessListener {
            isModelReady = true
        }
        .addOnFailureListener { exception ->

        }
    if(isModelReady) {
        descriptions.forEach {
            englishKoreanTranslator.translate(it)
                .addOnSuccessListener { translatedText += it }
                .addOnFailureListener { exception ->

                }
        }
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { pickMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly)) },
            modifier = Modifier.align(Alignment.CenterHorizontally),
            border = BorderStroke(1.dp, Color.Black)
        ) {
            if (image != null) {
                AsyncImage(
                    model = image,
                    contentDescription = "Picked photo",
                    contentScale = ContentScale.Crop
                )
            } else {
                Text("Pick a photo")
            }
        }

        if (image != null) {
            Button(
                onClick = {
                    viewModel.getCaption(
                        image = image!!,
                        context = context
                    )
                },
                modifier = Modifier.align(Alignment.CenterHorizontally),
                border = BorderStroke(1.dp, Color.Black)
            ) {
                Text("Explain Situation")
            }
        }

        if (descriptions.isNotEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                descriptions.forEach {
                    Text(it)
                }

                translatedDescriptions.forEach {
                    Text(it)
                }
            }
        }
    }
}
