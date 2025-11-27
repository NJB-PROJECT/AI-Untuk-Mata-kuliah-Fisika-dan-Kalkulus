package com.njbproject.ai.fiscall.ui

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.njbproject.ai.fiscall.data.api.GeminiHelper
import com.njbproject.ai.fiscall.data.local.AppDatabase
import com.njbproject.ai.fiscall.data.model.ChatMessage
import com.njbproject.ai.fiscall.utils.Constants
import com.njbproject.ai.fiscall.utils.PreferenceManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val chatDao = db.chatDao()
    val allMessages: LiveData<List<ChatMessage>> = chatDao.getAllMessages()

    private fun getGeminiHelper(): GeminiHelper {
        val context = getApplication<Application>()
        val isUserKey = PreferenceManager.isUserApiKey(context)
        val apiKey = if (isUserKey) {
            PreferenceManager.getUserApiKey(context)
        } else {
            Constants.BUILT_IN_API_KEY
        }
        val modelName = PreferenceManager.getModelName(context)

        return GeminiHelper(apiKey, modelName)
    }

    fun sendMessage(text: String, imagePath: String? = null) {
        viewModelScope.launch {
            // 1. Save User Message
            val userMsg = ChatMessage(text = text, isUser = true, imagePath = imagePath)
            chatDao.insertMessage(userMsg)

            // 2. Prepare AI Request
            val context = getApplication<Application>()
            val systemPrompt = PreferenceManager.getSystemPrompt(context)
            val helper = getGeminiHelper()

            var image: Bitmap? = null
            if (imagePath != null) {
                withContext(Dispatchers.IO) {
                    try {
                        image = BitmapFactory.decodeFile(imagePath)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            // 3. Call API
            // Insert a placeholder "Typing..." message if you wanted, but for now we just wait
            // Better UX: Add a loading state in UI.

            val responseText = withContext(Dispatchers.IO) {
                helper.generateContent(text, image, systemPrompt)
            }

            // 4. Save AI Response
            val aiMsg = ChatMessage(text = responseText, isUser = false)
            chatDao.insertMessage(aiMsg)
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            chatDao.clearHistory()
        }
    }
}
