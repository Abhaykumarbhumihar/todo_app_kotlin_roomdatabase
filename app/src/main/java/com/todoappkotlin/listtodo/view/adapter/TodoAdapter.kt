package com.todoappkotlin.listtodo.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.todoappkotlin.databinding.TodoActiveListBinding
import com.todoappkotlin.room.TodoWithCategory

class TodoAdapter(private var todoList: List<TodoWithCategory>) :
    RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    inner class TodoViewHolder(private val binding: TodoActiveListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        // Binding the views using the binding object
        fun bind(todoWithCategory: TodoWithCategory) {
            binding.todoName.text = todoWithCategory.todo.taskName
            binding.todoDate.text =
                "Due: ${todoWithCategory.todo.date}" // Assuming date is part of TodoEntity
            binding.todoTime.text =
                "Time: ${todoWithCategory.todo.time}" // Assuming time is part of TodoEntity

            // Set checkbox state if needed
            binding.todoCheckbox.isChecked = false // Set to true if the task is completed

            // Optional: If you want to handle checkbox changes
            binding.todoCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
                // Handle checkbox state change if necessary
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val binding =
            TodoActiveListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TodoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val todoWithCategory = todoList[position]
        holder.bind(todoWithCategory) // Bind the data to the ViewHolder
    }

    override fun getItemCount(): Int {
        return todoList.size
    }

    fun setTodoList(newTodoList: List<TodoWithCategory>) {
        todoList = newTodoList
        notifyDataSetChanged() // Notify that data has changed
    }
}
