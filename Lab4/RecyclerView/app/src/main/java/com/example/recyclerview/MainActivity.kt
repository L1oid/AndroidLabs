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

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Создаем список с экземплярами класса ColorData
        val colorDataList = ArrayList<ColorData>()
        colorDataList.add(ColorData("Красный", 0xFFFF0000.toInt())) // Красный цвет в формате ARGB
        colorDataList.add(ColorData("Зеленый", 0xFF00FF00.toInt())) // Зеленый цвет в формате ARGB
        colorDataList.add(ColorData("Синий", 0xFF0000FF.toInt())) // Синий цвет в формате ARGB
        colorDataList.add(ColorData("Желтый", 0xFFFFFF00.toInt())) // Желтый цвет в формате ARGB
        colorDataList.add(ColorData("Фиолетовый", 0xFF800080.toInt())) // Фиолетовый цвет в формате ARGB


        // Находим RecyclerView по его идентификатору
        val recyclerView: RecyclerView = findViewById(R.id.rView)

        // Создаем адаптер и передаем в него список с данными
        val adapter = Adapter(this, colorDataList)

        // Устанавливаем адаптер для RecyclerView
        recyclerView.adapter = adapter

        // Устанавливаем менеджер компоновки для RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
    }
}

data class ColorData(val colorName: String, val colorHex: Int)

class Adapter(private val context: Context, private val list: ArrayList<ColorData>) :
    RecyclerView.Adapter<Adapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.rview_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.colorNameTextView.text = item.colorName
        holder.squareView.setBackgroundColor(item.colorHex)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val colorNameTextView: TextView = itemView.findViewById(R.id.text_view)
        val squareView: View = itemView.findViewById(R.id.square_view)
    }
}
