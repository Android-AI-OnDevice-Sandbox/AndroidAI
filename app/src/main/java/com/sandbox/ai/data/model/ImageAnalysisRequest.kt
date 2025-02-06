package com.sandbox.ai.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ImageAnalysisRequest(
    val url: String
)