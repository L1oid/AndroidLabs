package com.example.logging

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import timber.log.Timber
import timber.log.Timber.DebugTree


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val buttonLog = findViewById<Button>(R.id.button_log)
        val buttonTimber = findViewById<Button>(R.id.button_timber)
        val myEditText = findViewById<EditText>(R.id.my_edit_text)
        buttonLog.setOnClickListener {
            Log.v("From EditText", myEditText.text.toString())
        }
        Timber.plant(DebugTree())
        buttonTimber.setOnClickListener {
            Timber.v(myEditText.text.toString())
        }
    }
}