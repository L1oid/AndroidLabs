package com.example.nestedlayouts

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    private var count = 1
    private lateinit var group1: Array<TextView>
    private lateinit var group2: Array<TextView>
    private lateinit var group3: Array<TextView>
    private var currentGroupIndex = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        group1 = arrayOf(
            findViewById(R.id.textView1),
            findViewById(R.id.textView4),
            findViewById(R.id.textView7)
        )
        group2 = arrayOf(
            findViewById(R.id.textView2),
            findViewById(R.id.textView5),
            findViewById(R.id.textView8)
        )
        group3 = arrayOf(
            findViewById(R.id.textView3),
            findViewById(R.id.textView6),
            findViewById(R.id.textView9)
        )
        val rollButton = findViewById<Button>(R.id.button)
        rollButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                count++
                currentGroupIndex++
                if (currentGroupIndex >= 3) {
                    currentGroupIndex = 0
                }

                updateGroupValues(currentGroup)
            }
        })
    }

    private val currentGroup: Array<TextView>
        private get() = if (currentGroupIndex == 0) {
            group1
        } else if (currentGroupIndex == 1) {
            group2
        } else {
            group3
        }

    private fun updateGroupValues(group: Array<TextView>) {
        clearGroup(getPreviousGroup())
        for (textView in group) {
            textView.text = count.toString()
        }
    }

    private fun getPreviousGroup(): Array<TextView> {
        var previousGroupIndex = currentGroupIndex - 1
        if (previousGroupIndex < 0) {
            previousGroupIndex = 2
        }
        return if (previousGroupIndex == 0) {
            group1
        } else if (previousGroupIndex == 1) {
            group2
        } else {
            group3
        }
    }

    private fun clearGroup(group: Array<TextView>) {
        for (textView in group) {
            textView.text = ""
        }
    }
}



