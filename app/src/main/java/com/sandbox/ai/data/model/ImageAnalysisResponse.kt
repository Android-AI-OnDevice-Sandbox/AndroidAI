package com.sandbox.ai.data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ImageAnalysisResponse(
    @SerialName("denseCaptionsResult") val denseCaptionsResult: DenseCaptionsResult,
    @SerialName("metadata") val metadata: Metadata,
    @SerialName("modelVersion") val modelVersion: String
)