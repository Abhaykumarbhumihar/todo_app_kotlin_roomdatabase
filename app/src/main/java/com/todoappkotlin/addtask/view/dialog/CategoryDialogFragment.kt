package com.todoappkotlin.addtask.view.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.todoappkotlin.R


import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment



class CategoryDialogFragment : DialogFragment() {

    // Define a callback interface
    interface CategoryDialogListener {
        fun onCategoryAdded(categoryName: String)
    }

    private var listener: CategoryDialogListener? = null


    fun setListener(listener: CategoryDialogListener) {
        this.listener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_category_dialog, container, false)

        val editTextCategoryName = view.findViewById<EditText>(R.id.editTextCategoryName)
        val buttonAdd = view.findViewById<Button>(R.id.buttonAdd)
        val buttonCancel = view.findViewById<Button>(R.id.buttonCancel)

        buttonAdd.setOnClickListener {
            val categoryName = editTextCategoryName.text.toString()
            if (categoryName.isNotEmpty()) {
                listener?.onCategoryAdded(categoryName) // Pass the category name back
                dismiss() // Close the dialog
            } else {
                editTextCategoryName.error = "Please enter a category name"
            }
        }

        buttonCancel.setOnClickListener {
            dismiss() // Close the dialog without adding
        }

        return view
    }
}
