package com.example.newactivity

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class PicActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pic_layout)

        val intent = intent
        val picLink = intent.getStringExtra("picLink")
        val picView = findViewById<ImageView>(R.id.picView)

        title = "Картинка"

        Glide.with(this)
            .load(picLink)
            .into(picView)
    }
}
