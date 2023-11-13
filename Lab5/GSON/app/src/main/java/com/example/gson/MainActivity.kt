package com.example.gson

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

data class Photo(
    val id: String,
    val owner: String,
    val secret: String,
    val server: String,
    val farm: Int,
    val title: String,
    val ispublic: Int,
    val isfriend: Int,
    val isfamily: Int
) {
    fun getImageUrl(): String {
        return "https://farm${farm}.staticflickr.com/${server}/${id}_${secret}_z.jpg"
    }
}

data class PhotoPage(
    val page: Int,
    val pages: Int,
    val perpage: Int,
    val total: Int,
    val photo: List<Photo>
)

data class Wrapper(
    val photos: PhotoPage,
    val stat: String
)

class MainActivity : AppCompatActivity() {
    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Timber.plant(Timber.DebugTree())

        GlobalScope.launch(Dispatchers.IO) {
            val result = doInBackground()
            runOnUiThread {
                parseJson(result)
            }
        }
    }

    private fun doInBackground(vararg params: Void?): String {
        val url = URL("https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=ff49fcd4d4a08aa6aafb6ea3de826464&tags=cat&format=json&nojsoncallback=1")
        val connection = url.openConnection() as HttpURLConnection
        try {
            val reader = BufferedReader(InputStreamReader(connection.inputStream))
            val response = StringBuilder()
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                response.append(line)
            }
            return response.toString()
        } finally {
            connection.disconnect()
        }
    }

    private fun parseJson(result: String) {
        try {
            val recyclerView: RecyclerView = findViewById(R.id.rView)
            val layoutManager = GridLayoutManager(this, 2)
            recyclerView.layoutManager = layoutManager

            val gson = Gson()
            val wrapper = gson.fromJson(result, Wrapper::class.java)
            wrapper.photos.photo.forEachIndexed { index, photo ->
                if ((index + 1) % 5 == 0) {
                    Timber.d("Photo ${index + 1}: $photo")
                }
            }


            val adapter = PhotoAdapter(this, wrapper.photos.photo)
            recyclerView.adapter = adapter

        } catch (e: Exception) {
            Timber.e(e, "Error parsing JSON")
        }
    }
}

class PhotoAdapter(private val context: Context, private val photoList: List<Photo>) :
    RecyclerView.Adapter<PhotoAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.rview_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val photo = photoList[position]

        Glide.with(context)
            .load(photo.getImageUrl())
            .into(holder.imageView)


        holder.itemView.setOnClickListener {
            copyToClipboard(photo.getImageUrl())
            Timber.i("Image URL copied to clipboard: ${photo.getImageUrl()}")
        }
    }

    override fun getItemCount(): Int {
        return photoList.size
    }

    private fun copyToClipboard(text: String) {
        val clipboardManager =
            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("Image URL", text)
        clipboardManager.setPrimaryClip(clipData)
    }
}