package com.example.newactivity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btn_show_pic).setOnClickListener {
            val intent = Intent(this, PicActivity::class.java)
            val picLink = "https://img.freepik.com/free-photo/a-cupcake-with-a-strawberry-on-top-and-a-strawberry-on-the-top_1340-35087.jpg"
            intent.putExtra("picLink", picLink)
            startActivity(intent)
        }
    }
}
