package com.todoappkotlin.listtodo.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.todoappkotlin.databinding.TodoActiveListBinding
import com.todoappkotlin.room.TodoWithCategory

class TodoAdapter(private var todoList: List<TodoWithCategory>,  private val listener: OnTodoItemCheckedChangeListener ) :
    RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    inner class TodoViewHolder(private val binding: TodoActiveListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        // Binding the views using the binding object
        fun bind(todoWithCategory: TodoWithCategory) {
            binding.todoName.text = todoWithCategory.todo.taskName
            binding.todoDate.text =
                "Due: ${todoWithCategory.todo.date}"
            binding.todoTime.text =
                "Time: ${todoWithCategory.todo.time}"



            binding.todoCheckbox.isChecked = todoWithCategory.todo.status



            binding.todoCheckbox.setOnCheckedChangeListener { _, isChecked ->
                listener.onTodoItemCheckedChange(todoWithCategory.todo.id, isChecked,todoWithCategory )
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

    interface OnTodoItemCheckedChangeListener {
        fun onTodoItemCheckedChange(todoId: Int, isChecked: Boolean,currentData: TodoWithCategory)
    }
}
