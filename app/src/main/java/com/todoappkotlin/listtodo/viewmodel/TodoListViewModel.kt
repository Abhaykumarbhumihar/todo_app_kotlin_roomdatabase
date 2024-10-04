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


    fun updateTodoStatus(todoId: Int, status: Boolean, callback: TodoListCallBack<String>) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                respositroy.updateTodoStatus(todoId, status)
                if(status){
                    callback.onSuccess("Task updated as complete")
                }else{
                    callback.onSuccess("Task updated as in-complete")
                }

            } catch (e: Exception) {
                callback.onError(e)
            }
        }
    }

}