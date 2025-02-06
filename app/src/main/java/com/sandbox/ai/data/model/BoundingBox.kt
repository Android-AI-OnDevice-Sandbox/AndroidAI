package com.sandbox.ai.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BoundingBox(
    @SerialName("h") val h: Int,
    @SerialName("w") val w: Int,
    @SerialName("x") val x: Int,
    @SerialName("y") val y: Int
)