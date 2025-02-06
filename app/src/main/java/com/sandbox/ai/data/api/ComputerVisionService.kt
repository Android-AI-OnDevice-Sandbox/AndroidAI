package com.sandbox.ai.data.api

import com.sandbox.ai.BuildConfig
import com.sandbox.ai.data.model.ImageAnalysisResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

class ComputerVisionService(
    private val endPoint: String = BuildConfig.AZURE_ENDPOINT,
    private val subscriptionKey: String = BuildConfig.AZURE_SUBSCRIPTION_KEY
) {
    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        encodeDefaults = true
        isLenient = true
        prettyPrint = true
    }

    private val retrofit = Retrofit.Builder()
        .baseUrl(endPoint)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    private val api = retrofit.create(ComputerVisionApi::class.java)

//    suspend fun analyzeImage(
//        image: String
//    ): ImageAnalysisResponse {
//        return withContext(Dispatchers.IO) {
//            try {
//                api.analyzeImage(
//                    subscriptionKey = subscriptionKey,
//                    request = ImageAnalysisRequest(image)
//                )
//            } catch (e: Exception) {
//                throw when (e) {
//                    is HttpException -> when (e.code()) {
//                        400 -> IllegalArgumentException("잘못된 요청입니다: ${e.message()}")
//                        401 -> SecurityException("인증에 실패했습니다. API 키를 확인해주세요.")
//                        404 -> NoSuchElementException("리소스를 찾을 수 없습니다.")
//                        else -> e
//                    }
//
//                    else -> e
//                }
//            }
//        }
//    }

    suspend fun analyzeLocalImage(imageBytes: ByteArray): ImageAnalysisResponse {
        return withContext(Dispatchers.IO) {
            try {
                val requestBody = imageBytes.toRequestBody("application/octet-stream".toMediaType())
                api.analyzeImage(
                    subscriptionKey = subscriptionKey,
                    request = requestBody
                )
            } catch (e: Exception) {
                throw when (e) {
                    is HttpException -> when (e.code()) {
                        400 -> IllegalArgumentException("잘못된 요청입니다: ${e.message()}")
                        401 -> SecurityException("인증에 실패했습니다. API 키를 확인해주세요.")
                        404 -> NoSuchElementException("리소스를 찾을 수 없습니다.")
                        else -> e
                    }
                    else -> e
                }
            }
        }
    }
}