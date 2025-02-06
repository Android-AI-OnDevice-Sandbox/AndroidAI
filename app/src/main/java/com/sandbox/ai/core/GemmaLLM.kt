package com.sandbox.ai.core

import android.content.Context
import com.google.mediapipe.tasks.genai.llminference.LlmInference

class GemmaLLM(context: Context, description: String) {
    private val options = LlmInference.LlmInferenceOptions.builder()
        .setModelPath("/data/local/tmp/llm/gemma-2b-it-gpu-int4.bin")
        .setMaxTokens(10000)
        .setTopK(40)
        .setTemperature(0.8f)
        .setRandomSeed(101)
        .build()

    private val inputPrompt = """Make a story about the description below.
        $description
    """.trimMargin()

    private val llmInference: LlmInference = LlmInference.createFromOptions(context, options)

    fun getResult(): String = llmInference.generateResponse(inputPrompt)
}