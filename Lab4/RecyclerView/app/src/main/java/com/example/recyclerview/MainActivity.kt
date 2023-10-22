package com.example.recyclerview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList
import androidx.recyclerview.widget.LinearLayoutManager
import android.widget.Toast

interface CellClickListener {
    fun onCellClick(colorName: String)
}

class MainActivity : AppCompatActivity(), CellClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val colorDataList = ArrayList<ColorData>()
        colorDataList.add(ColorData("Red", 0xFFFF0000.toInt()))
        colorDataList.add(ColorData("Green", 0xFF00FF00.toInt()))
        colorDataList.add(ColorData("Blue", 0xFF0000FF.toInt()))
        colorDataList.add(ColorData("Yellow", 0xFFFFFF00.toInt()))
        colorDataList.add(ColorData("Purple", 0xFF800080.toInt()))

        val recyclerView: RecyclerView = findViewById(R.id.rView)
        val adapter = Adapter(this, colorDataList, this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onCellClick(colorName: String) {
        Toast.makeText(this, "IT'S $colorName", Toast.LENGTH_SHORT).show()
    }
}

data class ColorData(val colorName: String, val colorHex: Int)

class Adapter(private val context: Context, private val list: ArrayList<ColorData>, private val cellClickListener: CellClickListener) :
    RecyclerView.Adapter<Adapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.rview_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.colorNameTextView.text = item.colorName
        holder.squareView.setBackgroundColor(item.colorHex)

        holder.itemView.setOnClickListener {
            cellClickListener.onCellClick(item.colorName)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val colorNameTextView: TextView = itemView.findViewById(R.id.text_view)
        val squareView: View = itemView.findViewById(R.id.square_view)
    }
}
