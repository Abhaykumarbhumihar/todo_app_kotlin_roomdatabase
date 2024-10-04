package com.todoappkotlin.category.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class CategorySpinnerAdapter(context: Context, categories: List<String>) : ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, categories) {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getDropDownView(position, convertView, parent)
        val textView = view.findViewById<TextView>(android.R.id.text1)

        // Highlight "Select Task Category" by changing its text color
        if (getItem(position) == "Select Task Category") {
            textView.setTextColor(context.getColor(android.R.color.holo_blue_light)) // or any color you want
        } else {
            textView.setTextColor(context.getColor(android.R.color.black))
        }
        return view
    }
}
