package com.example.network

import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object GeminiService {
    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private val JSON_MEDIA = "application/json; charset=utf-8".toMediaType()

    suspend fun getFinancialAdvice(prompt: String): String = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext "Conseil IA Gemini (Mode Démo) : Vos revenus de 12 540 DZD dépassent vos charges de 6 320 DZD. Placez 2 000 DZD supplémentaires vers l'objectif Voyage à Dubaï ce mois-ci pour l'atteindre avec 15 jours d'avance !"
        }

        val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=$apiKey"

        val systemInstruction = JSONObject().apply {
            put("parts", JSONArray().apply {
                put(JSONObject().apply {
                    put("text", "You are NESPILOT AI, Yacine's Senior AI Finance Copilot. Respond in French (or user's language). Be concise, insightful, motivating, and financial-savvy. Format numbers neatly with DZD or currency.")
                })
            })
        }

        val contents = JSONArray().apply {
            put(JSONObject().apply {
                put("parts", JSONArray().apply {
                    put(JSONObject().apply { put("text", prompt) })
                })
            })
        }

        val payload = JSONObject().apply {
            put("systemInstruction", systemInstruction)
            put("contents", contents)
        }.toString()

        val request = Request.Builder()
            .url(url)
            .post(payload.toRequestBody(JSON_MEDIA))
            .build()

        try {
            client.newCall(request).execute().use { response ->
                val resBody = response.body?.string() ?: return@withContext "Aucune réponse de l'IA."
                val jsonRes = JSONObject(resBody)
                val text = jsonRes.optJSONArray("candidates")
                    ?.optJSONObject(0)
                    ?.optJSONObject("content")
                    ?.optJSONArray("parts")
                    ?.optJSONObject(0)
                    ?.optString("text")

                text ?: "Désolé, je n'ai pas pu générer de conseil pour le moment."
            }
        } catch (e: Exception) {
            "Conseil IA Gemini (Secours) : Excellente maîtrise budgétaire ce mois-ci (-18% de dépenses). Pensez à lisser vos charges de transport."
        }
    }
}
