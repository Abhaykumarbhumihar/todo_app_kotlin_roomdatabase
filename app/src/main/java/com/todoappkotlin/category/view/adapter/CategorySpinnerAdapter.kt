package com.todoappkotlin.category.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.todoappkotlin.room.CategoryEntity

class CategorySpinnerAdapter(context: Context, categories: List<CategoryEntity>) :
    ArrayAdapter<CategoryEntity>(context, android.R.layout.simple_spinner_item, categories) {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    // Override the getView method to customize the view of the selected item
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)
        val textView = view.findViewById<TextView>(android.R.id.text1)

        // Set the category name as the text for the selected item
        textView.text = getItem(position)?.categoryName

        return view
    }

    // Override the getDropDownView method to customize the dropdown items
    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getDropDownView(position, convertView, parent)
        val textView = view.findViewById<TextView>(android.R.id.text1)

        // Set the category name for dropdown items
        textView.text = getItem(position)?.categoryName

        // Highlight "Select Task Category" by changing its text color
        if (getItem(position)?.categoryName == "Select Task Category") {
            textView.setTextColor(context.getColor(android.R.color.holo_blue_light)) // or any color you want
        } else {
            textView.setTextColor(context.getColor(android.R.color.black))
        }

        return view
    }
}
