package com.example.attributes

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.widget.Button
import android.widget.EditText

class MainActivity : AppCompatActivity() {
    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val editText = findViewById<EditText>(R.id.my_edit_text)
        val buttonBlackText = findViewById<Button>(R.id.button_black_text)
        val buttonRedText = findViewById<Button>(R.id.button_red_text)
        val buttonSize8SP = findViewById<Button>(R.id.button_size_8sp)
        val buttonSize24SP = findViewById<Button>(R.id.button_size_24sp)
        val buttonWhiteBack = findViewById<Button>(R.id.button_white_back)
        val buttonYellowBack = findViewById<Button>(R.id.button_yellow_back)
        buttonBlackText.setOnClickListener {
            editText.setTextColor(Color.BLACK)
        }
        buttonRedText.setOnClickListener {
            editText.setTextColor(Color.RED)
        }
        buttonSize8SP.setOnClickListener {
            editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 8F)
        }
        buttonSize24SP.setOnClickListener {
            editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24F)
        }
        buttonWhiteBack.setOnClickListener {
            editText.setBackgroundColor(Color.WHITE)
        }
        buttonYellowBack.setOnClickListener {
            editText.setBackgroundColor(Color.YELLOW)
        }
    }
}