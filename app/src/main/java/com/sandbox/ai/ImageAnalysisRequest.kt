package com.sandbox.ai

import kotlinx.serialization.Serializable

@Serializable
data class ImageAnalysisRequest(
    val url: String
)