package com.njbproject.ai.fiscall.data.api

import android.graphics.Bitmap
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.Content
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
                // Use a helper function to isolate the DSL and avoid compiler crashes
                val inputContent = createImageContent(image, finalPrompt)
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

    // Isolate the DSL call to avoid FirIncompatibleClassExpressionChecker crash
    // Renamed parameter 'image' to 'bitmap' to avoid shadowing the 'image()' DSL function
    private fun createImageContent(bitmap: Bitmap, prompt: String): Content {
        return content {
            image(bitmap)
            text(prompt)
        }
    }
}
