package com.example.finance_project.data.repository

import android.content.Context
import android.util.Log
import com.example.finance_project.data.model.TopicModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException

class TopicRepository(private val context: Context) {

    fun loadTopics(): List<TopicModel> {
        return try {
            val assetManager = context.assets
            val files = assetManager.list("") ?: emptyArray()
            Log.d("TopicRepository", "Assets in folder: ${files.joinToString()}")

            val jsonString = assetManager.open("topics.json")
                .bufferedReader()
                .use { it.readText() }

            Log.d("TopicRepository", "JSON loaded successfully. Length: ${jsonString.length}")

            val type = object : TypeToken<List<TopicModel>>() {}.type
            Gson().fromJson(jsonString, type)
        } catch (e: IOException) {
            Log.e("TopicRepository", "Error loading topics.json", e)
            emptyList()
        } catch (e: Exception) {
            Log.e("TopicRepository", "JSON parse error", e)
            emptyList()
        }
    }
}
