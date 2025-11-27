package com.njbproject.ai.fiscall.data.api

import android.graphics.Bitmap
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.njbproject.ai.fiscall.utils.Constants

class GeminiHelper(
    private val apiKey: String,
    private val modelName: String = "gemini-1.5-flash" // Default safe fallback
) {

    private val generativeModel = GenerativeModel(
        modelName = modelName,
        apiKey = apiKey
    )

    suspend fun generateContent(prompt: String, image: Bitmap? = null, systemInstruction: String? = null): String {
        // Note: SDK 0.9.0 might not support systemInstruction directly in constructor for all models or the way we want.
        // We will prepend the system instruction to the prompt if provided, which is a standard workaround
        // or re-initialize if the SDK supports it better (SDK 0.9.0 supports systemInstruction in config).

        // For simplicity and compatibility with older models, prepending is often safest unless we strictly use newer APIs.
        // However, let's try to use the systemInstruction if we were to initialize it.
        // Since we are creating the model here, let's stick to simple prompt injection for now
        // OR re-create the model if system instruction changes (expensive).

        // Better approach: Prepend the system prompt to the user's message.
        val finalPrompt = if (!systemInstruction.isNullOrBlank()) {
            "$systemInstruction\n\nUser: $prompt"
        } else {
            prompt
        }

        return try {
            val response = if (image != null) {
                generativeModel.generateContent(
                    content {
                        image(image)
                        text(finalPrompt)
                    }
                )
            } else {
                generativeModel.generateContent(finalPrompt)
            }
            response.text ?: "No response generated."
        } catch (e: Exception) {
            "Error: ${e.localizedMessage}"
        }
    }
}
