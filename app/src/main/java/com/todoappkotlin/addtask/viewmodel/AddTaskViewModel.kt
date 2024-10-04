package com.todoappkotlin.addtask.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.todoappkotlin.addtask.callback.CategoryCallback
import com.todoappkotlin.addtask.repository.AddTaskRepository
import com.todoappkotlin.room.CategoryEntity
import com.todoappkotlin.room.TodoEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddTaskViewModel(private val repository: AddTaskRepository) : ViewModel() {


    fun addCategory(category: CategoryEntity, callback: CategoryCallback<String>) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.addCategory(category)
                callback.onSuccess("Data added")
            } catch (e: Exception) {
                callback.onError(e)
            }
        }
    }

    fun getCategoryList(callback: CategoryCallback<List<CategoryEntity>>) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val categoryList = repository.getCategory()
                callback.onSuccess(categoryList)
            } catch (e: Exception) {
                callback.onError(e)
            }

        }
    }

    fun addTodoTask(todoEntity: TodoEntity, callback: CategoryCallback<String>) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.addTask(todoEntity)
                callback.onSuccess("Task Added")
            } catch (e: Exception) {
                callback.onError(e)
            }

        }
    }


}