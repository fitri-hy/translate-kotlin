package com.hytech.translateapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.ResponseBody
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var editText: EditText
    private lateinit var textView: TextView
    private lateinit var spinnerLanguage: Spinner
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editText = findViewById(R.id.editText)
        textView = findViewById(R.id.textView)
        spinnerLanguage = findViewById(R.id.spinner_language)
        progressBar = findViewById(R.id.progress_bar)
        progressBar.visibility = View.GONE
    }

    fun translateText(view: android.view.View) {
        val textToTranslate = editText.text.toString()
        val targetLanguage = getLanguageCodeFromSpinner()
        progressBar.visibility = View.VISIBLE

        CoroutineScope(Dispatchers.IO).launch {
            val translation = translate(textToTranslate, targetLanguage)

            withContext(Dispatchers.Main) {
                progressBar.visibility = View.GONE

                if (translation != null) {
                    textView.text = translation
                } else {
                    textView.text = "Terjemahan gagal"
                }
            }
        }
    }

    private fun getLanguageCodeFromSpinner(): String {
        val selectedLanguage = spinnerLanguage.selectedItem as String
        return when (selectedLanguage) {
            "Indonesia" -> "id"
            "Inggris" -> "en"
            "Jawa" -> "jv"
            "Sunda" -> "su"
            "Afrikaans" -> "af"
            "Albania" -> "sq"
            "Amharic" -> "am"
            "Arab" -> "ar"
            "Aragonese" -> "an"
            "Armenia" -> "hy"
            "Assamese" -> "as"
            "Avar" -> "av"
            "Avestan" -> "ae"
            "Azeri" -> "az"
            "Basque" -> "eu"
            "Belarusian" -> "be"
            "Bengali" -> "bn"
            "Bihari" -> "bh"
            "Bislama" -> "bi"
            "Bosnian" -> "bs"
            "Breton" -> "br"
            "Bulgarian" -> "bg"
            "Burmese" -> "my"
            "Catalan" -> "ca"
            "Cebuano" -> "ce"
            "Chichewa" -> "ny"
            "Chinese (Simplified)" -> "zh-Hans"
            "Chinese (Traditional)" -> "zh-Hant"
            "Corsican" -> "co"
            "Croatian" -> "hr"
            "Czech" -> "cs"
            "Danish" -> "da"
            "Dutch" -> "nl"
            "Esperanto" -> "eo"
            "Estonian" -> "et"
            "Ewe" -> "ee"
            "Filipino" -> "tl"
            "Finnish" -> "fi"
            "French" -> "fr"
            "Frisian" -> "fy"
            "Galician" -> "gl"
            "Georgian" -> "ka"
            "German" -> "de"
            "Greek" -> "el"
            "Gujarati" -> "gu"
            "Haitian Creole" -> "ht"
            "Hausa" -> "ha"
            "Hebrew" -> "he"
            "Hindi" -> "hi"
            "Hungarian" -> "hu"
            "Icelandic" -> "is"
            "Igbo" -> "bo"
            "Irish" -> "ga"
            "Italian" -> "it"
            "Japanese" -> "ja"
            "Javanese" -> "jv"
            "Kannada" -> "kn"
            "Kazakh" -> "kk"
            "Khmer" -> "km"
            "Korean" -> "ko"
            "Kurdish (Kurmanji)" -> "ku-rmd"
            "Kyrgyz" -> "kg"
            "Lao" -> "lo"
            "Latvian" -> "lv"
            "Lithuanian" -> "lt"
            "Luxembourgish" -> "lb"
            "Macedonian" -> "mk"
            "Malagasy" -> "mg"
            "Malay" -> "ms"
            "Malayalam" -> "ml"
            "Maltese" -> "mt"
            "Maori" -> "mi"
            "Marathi" -> "mr"
            "Mongolian" -> "mn"
            "Nepali" -> "ne"
            "Norwegian" -> "no"
            "Nyanja (Chichewa)" -> "ny"
            else -> "en"
        }
    }

    private suspend fun translate(text: String, targetLanguage: String): String? {
        withContext(Dispatchers.Main) {
            progressBar.visibility = View.VISIBLE
        }

        val url = "https://api.hy-tech.my.id/api/translate?text=$text&target=$targetLanguage"

        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .build()

        val response = client.newCall(request).execute()
        if (response.isSuccessful) {
            val responseBody = response.body() as ResponseBody
            val json = responseBody.string()

            val jsonObject = JSONObject(json)
            return jsonObject.getString("translation")
        } else {
            return null
        }
    }
}