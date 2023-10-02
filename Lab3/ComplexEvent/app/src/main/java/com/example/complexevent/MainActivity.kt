package com.example.complexevent

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var editText: EditText
    private lateinit var checkBox: CheckBox
    private lateinit var button: Button
    private lateinit var textView: TextView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editText = findViewById(R.id.editText)
        checkBox = findViewById(R.id.checkBox)
        button = findViewById(R.id.button)
        textView = findViewById(R.id.textView)
        progressBar = findViewById(R.id.progressBar)

        button.setOnClickListener {
            if (checkBox.isChecked) {
                // Получаем текущее значение ProgressBar
                val currentProgress = progressBar.progress
                // Увеличиваем значение ProgressBar на 10%
                val newProgress = currentProgress + (progressBar.max * 0.1).toInt()
                // Убедимся, что новое значение не превышает максимум
                progressBar.progress = newProgress.coerceAtMost(progressBar.max)

                // Устанавливаем текст из EditText в TextView
                val inputText = editText.text.toString()
                textView.text = inputText
            }
        }
    }
}

