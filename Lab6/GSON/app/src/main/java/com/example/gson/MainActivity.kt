package com.example.gson

import android.app.Activity
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
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar

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

    private var picViewerLauncher: ActivityResultLauncher<Intent>? = null

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

        picViewerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val imageUrl = result.data?.getStringExtra("imageUrl")
                if (imageUrl != null) {
                    val snackbar = Snackbar.make(
                        findViewById(android.R.id.content),
                        "Картинка добавлена в избранное",
                        Snackbar.LENGTH_LONG
                    )
                    snackbar.setAction("Открыть") {
                        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(imageUrl))
                        startActivity(browserIntent)
                    }
                    snackbar.show()
                }
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

            adapter.setOnItemClickListener { imageUrl ->
                openPicViewerActivity(imageUrl)
            }

        } catch (e: Exception) {
            Timber.e(e, "Error parsing JSON")
        }
    }

    private fun openPicViewerActivity(imageUrl: String) {
        val intent = Intent(this, PicViewerActivity::class.java)
        intent.putExtra("imageUrl", imageUrl)
        picViewerLauncher?.launch(intent)
    }
}

class PhotoAdapter(private val context: Context, private val photoList: List<Photo>) :
    RecyclerView.Adapter<PhotoAdapter.ViewHolder>() {

    private var onItemClickListener: ((String) -> Unit)? = null

    fun setOnItemClickListener(listener: (String) -> Unit) {
        onItemClickListener = listener
    }

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
            onItemClickListener?.invoke(photo.getImageUrl())
        }
    }

    override fun getItemCount(): Int {
        return photoList.size
    }
}