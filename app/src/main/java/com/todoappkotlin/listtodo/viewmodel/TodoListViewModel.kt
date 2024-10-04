package com.todoappkotlin.listtodo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.todoappkotlin.listtodo.callback.TodoListCallBack
import com.todoappkotlin.listtodo.repository.TodoListRepository
import com.todoappkotlin.room.TodoWithCategory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TodoListViewModel(private val respositroy: TodoListRepository) : ViewModel() {

    fun getActiveTodoList(callBack: TodoListCallBack<List<TodoWithCategory>>) {
        viewModelScope.launch(Dispatchers.IO) {

            try {
                val list = respositroy.getTodoList()
                callBack.onSuccess(list)

            } catch (e: Exception) {
                callBack.onError(e)
            }
        }
    }

}