package com.example.practice.repository

import android.content.Context
import androidx.core.content.edit

object SharedPreferencesHelper {
    fun save(context: Context, key: String, value: String) {
        val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit() { putString(key, value) }
    }

    fun get(context: Context, key: String): String? {
        val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString(key, null)
    }
}
