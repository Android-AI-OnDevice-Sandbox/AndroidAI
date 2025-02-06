package com.sandbox.ai.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DenseCaptionsResult(
    @SerialName("values") val values: List<Value>
)