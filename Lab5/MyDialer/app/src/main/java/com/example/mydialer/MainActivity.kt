package com.example.mydialer

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import timber.log.Timber
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import java.io.IOException
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

data class Contact(
    val name: String,
    val phone: String,
    val type: String
)

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var contactAdapter: ContactAdapter
    private lateinit var etSearch: EditText
    private lateinit var btnSearch: Button
    private lateinit var originalContacts: List<Contact>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Timber.plant(Timber.DebugTree())

        val json: String = readJsonFromAssets("phones.json")
        val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
        val listType = Types.newParameterizedType(List::class.java, Contact::class.java)
        val adapter: JsonAdapter<List<Contact>> = moshi.adapter(listType)

        try {
            val contacts: List<Contact>? = adapter.fromJson(json)

            originalContacts = contacts ?: emptyList()
            recyclerView = findViewById(R.id.rView)
            recyclerView.layoutManager = LinearLayoutManager(this)
            contactAdapter = contacts?.let { ContactAdapter(it) } ?: ContactAdapter(emptyList())
            recyclerView.adapter = contactAdapter

        } catch (e: IOException) {
            Timber.e(e, "Error parsing JSON")
        }

        etSearch = findViewById(R.id.et_search)
        btnSearch = findViewById(R.id.btn_search)
        btnSearch.setOnClickListener {
            filterContacts()
        }
    }


    private fun readJsonFromAssets(fileName: String): String {
        return try {
            assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (e: IOException) {
            Timber.e(e, "Error reading JSON from assets")
            ""
        }
    }

    private fun filterContacts() {
        val searchText = etSearch.text.toString().trim()

        if (searchText.isEmpty()) {
            contactAdapter.setData(originalContacts)
        } else {
            val filteredContacts = originalContacts.filter {
                it.name.contains(searchText, ignoreCase = true) ||
                        it.phone.contains(searchText, ignoreCase = true) ||
                        it.type.contains(searchText, ignoreCase = true)
            }
            contactAdapter.setData(filteredContacts)
        }
    }
}

class ContactAdapter(private var contacts: List<Contact>) : RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textName: TextView = itemView.findViewById(R.id.textName)
        val textPhone: TextView = itemView.findViewById(R.id.textPhone)
        val textType: TextView = itemView.findViewById(R.id.textType)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.rview_item, parent, false)
        return ContactViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contacts[position]
        holder.textName.text = contact.name
        holder.textPhone.text = contact.phone
        holder.textType.text = contact.type
    }

    override fun getItemCount(): Int {
        return contacts.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newContacts: List<Contact>) {
        contacts = newContacts
        notifyDataSetChanged()
    }
}