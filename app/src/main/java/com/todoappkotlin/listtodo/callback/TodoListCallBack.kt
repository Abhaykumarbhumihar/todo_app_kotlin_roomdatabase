package com.todoappkotlin.listtodo.callback



interface TodoListCallBack<T> {
    fun onSuccess(categories: T)
    fun onError(exception: Exception)
}