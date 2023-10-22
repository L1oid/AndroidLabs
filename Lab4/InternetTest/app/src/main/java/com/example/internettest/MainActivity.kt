package com.example.internettest

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.io.IOException

class MainActivity : AppCompatActivity() {
    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnHTTP = findViewById<Button>(R.id.btnHTTP)
        btnHTTP.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) {
                makeHttpRequest()
            }
        }

        val btnOkHTTP = findViewById<Button>(R.id.btnOkHTTP)
        btnOkHTTP.setOnClickListener {
            makeOkHttpRequest()
        }
    }

    private fun makeHttpRequest() {
        val urlString = "https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=ff49fcd4d4a08aa6aafb6ea3de826464&tags=cat&format=json&nojsoncallback=1"

        val url = URL(urlString)
        val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"

        val reader = BufferedReader(InputStreamReader(connection.inputStream))
        val response = StringBuilder()
        var line: String?

        while (reader.readLine().also { line = it } != null) {
            response.append(line)
        }

        reader.close()
        connection.disconnect()
        Log.d("Flickr cats", response.toString())
    }

    private fun makeOkHttpRequest() {
        val url = "https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=ff49fcd4d4a08aa6aafb6ea3de826464&tags=cat&format=json&nojsoncallback=1"
        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("Flickr OkCats", "Error: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseData = response.body?.string()
                    Log.i("Flickr OkCats", responseData ?: "Empty response")
                } else {
                    Log.e("Flickr OkCats", "Error: ${response.code}")
                }
            }
        })
    }
}