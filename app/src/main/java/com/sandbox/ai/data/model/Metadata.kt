package com.sandbox.ai.data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Metadata(
    @SerialName("height") val height: Int,
    @SerialName("width") val width: Int
)