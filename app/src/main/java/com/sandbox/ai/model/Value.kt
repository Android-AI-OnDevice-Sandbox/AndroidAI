package com.sandbox.ai.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Value(
    @SerialName("boundingBox") val boundingBox: BoundingBox,
    @SerialName("confidence") val confidence: Double,
    @SerialName("text") val text: String
)