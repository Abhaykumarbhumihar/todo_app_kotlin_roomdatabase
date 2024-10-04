package com.todoappkotlin.listtodo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.todoappkotlin.listtodo.repository.TodoListRepository
import java.lang.IllegalArgumentException

class TodoListViewModelFactory(private val repository: TodoListRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TodoListViewModel::class.java)) {
            return TodoListViewModel(respositroy = repository) as T
        }
        throw IllegalArgumentException("Unknow ViewModel class")
    }
}