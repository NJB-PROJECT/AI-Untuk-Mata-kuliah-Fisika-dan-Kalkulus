package com.njbproject.ai.fiscall.data.api

import android.graphics.Bitmap
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.njbproject.ai.fiscall.utils.Constants

class GeminiHelper(
    private val apiKey: String,
    private val modelName: String = "gemini-1.5-flash"
) {

    private val generativeModel = GenerativeModel(
        modelName = modelName,
        apiKey = apiKey
    )

    suspend fun generateContent(prompt: String, image: Bitmap? = null, systemInstruction: String? = null): String {
        // Prepend system instruction if exists
        val finalPrompt = if (!systemInstruction.isNullOrBlank()) {
            "$systemInstruction\n\nUser: $prompt"
        } else {
            prompt
        }

        return try {
            if (image != null) {
                // Explicitly unwrap and use separate block to help compiler
                val safeImage: Bitmap = image
                val inputContent = content {
                    image(safeImage)
                    text(finalPrompt)
                }
                val response = generativeModel.generateContent(inputContent)
                response.text ?: "No response generated."
            } else {
                val response = generativeModel.generateContent(finalPrompt)
                response.text ?: "No response generated."
            }
        } catch (e: Exception) {
            "Error: ${e.localizedMessage}"
        }
    }
}
