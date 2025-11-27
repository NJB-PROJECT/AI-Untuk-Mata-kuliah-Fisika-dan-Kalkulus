package com.njbproject.ai.fiscall.utils

import android.content.Context
import android.content.SharedPreferences

object PreferenceManager {
    private const val PREF_NAME = "fiscall_prefs"
    private const val KEY_API_KEY_TYPE = "api_key_type" // 0 = Built-in, 1 = User
    private const val KEY_USER_API_KEY = "user_api_key"
    private const val KEY_MODEL_NAME = "model_name"
    private const val KEY_SYSTEM_PROMPT = "system_prompt"

    fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun saveApiKeyType(context: Context, isUser: Boolean) {
        getPreferences(context).edit().putInt(KEY_API_KEY_TYPE, if (isUser) 1 else 0).apply()
    }

    fun isUserApiKey(context: Context): Boolean {
        return getPreferences(context).getInt(KEY_API_KEY_TYPE, 0) == 1
    }

    fun saveUserApiKey(context: Context, key: String) {
        getPreferences(context).edit().putString(KEY_USER_API_KEY, key).apply()
    }

    fun getUserApiKey(context: Context): String {
        return getPreferences(context).getString(KEY_USER_API_KEY, "") ?: ""
    }

    fun saveModelName(context: Context, model: String) {
        getPreferences(context).edit().putString(KEY_MODEL_NAME, model).apply()
    }

    fun getModelName(context: Context): String {
        return getPreferences(context).getString(KEY_MODEL_NAME, "gemini-1.5-flash") ?: "gemini-1.5-flash"
    }

    fun saveSystemPrompt(context: Context, prompt: String) {
        getPreferences(context).edit().putString(KEY_SYSTEM_PROMPT, prompt).apply()
    }

    fun getSystemPrompt(context: Context): String {
        return getPreferences(context).getString(KEY_SYSTEM_PROMPT, Constants.DEFAULT_SYSTEM_PROMPT) ?: Constants.DEFAULT_SYSTEM_PROMPT
    }
}
