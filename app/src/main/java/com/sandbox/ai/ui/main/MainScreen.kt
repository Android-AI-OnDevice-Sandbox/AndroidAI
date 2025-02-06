package com.sandbox.ai.ui.main

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage

@Composable
fun PickPhotoScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel
) {
    var image: Uri? by rememberSaveable { mutableStateOf(null) }
    val context = LocalContext.current

    val descriptions by viewModel.captions.collectAsState()
    val translatedDescriptions by viewModel.translatedCaptions.collectAsState()

    val pickMedia = rememberLauncherForActivityResult(PickVisualMedia()) { uri ->
        if (uri != null) {
            image = uri
        }
    }

    Column(
        modifier = modifier.padding(vertical = 20.dp, horizontal = 16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = image,
            contentDescription = "Picked photo",
            modifier = Modifier.width(300.dp).height(300.dp).border(
                width = 1.dp,
                color = Color.LightGray,
                shape = RoundedCornerShape(8.dp)
            ),
            contentScale = ContentScale.FillBounds
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { pickMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly)) },
            modifier = Modifier.align(Alignment.CenterHorizontally),
            border = BorderStroke(1.dp, Color.Black)
        ) {
            Text("Pick a photo")
        }

        Spacer(modifier = Modifier.height(16.dp))

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

                Text(translatedDescriptions ?: "Error")
            }
        }
    }
}
